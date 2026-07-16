package com.mak.pocketnotes.core.feature.data.home.repository

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.core.common.models.SyncRequest
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.CuratedPodcastDAO
import com.mak.pocketnotes.core.database.dao.CuratedPodcastEntity
import com.mak.pocketnotes.core.database.dao.CuratedSectionEntity
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.database.dao.PodcastEntity
import com.mak.pocketnotes.core.database.queries.CuratedSectionWithPodcast
import com.mak.pocketnotes.core.feature.data.utils.exception
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcastsParam
import com.mak.pocketnotes.core.feature.domain.home.models.SectionPodcast
import com.mak.pocketnotes.core.feature.domain.home.repository.CuratedPodcastRepository
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.core.remote.dto.SectionPodcastDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.Validator
import kotlin.time.Duration.Companion.minutes

internal class OfflineFirstCuratedPodcastRepository(
    private val api: PocketNotesAPI,
    private val transactionRunner: DatabaseTransactionRunner,
    private val podcastDAO: PodcastDAO,
    private val curatedPodcastDAO: CuratedPodcastDAO,
    private val lastSyncDAO: LastSyncDAO,
    private val dispatcher: DispatcherProvider
) : CuratedPodcastRepository {

    private val store by lazy {
        StoreBuilder
            .from<CuratedPodcastsParam, List<SectionPodcastDTO>, List<CuratedPodcast>>(
                fetcher = Fetcher.of { param ->
                    api.getCuratedPodcasts(param.page)
                        .getOrThrow().curatedLists.orEmpty()
                },
                sourceOfTruth = SourceOfTruth.of(
                    reader = { param ->
                        observePodcasts(param)
                    },
                    writer = { param, dto ->
                        updateCuratedPodcasts(dto, param)
                    },
                    deleteAll = {
                        deleteAll()
                    },
                    delete = { param ->
                        delete(param)
                    }
                )
            ).validator(
                Validator.by { podcasts ->
                    return@by needsRefresh(podcasts)
                }
            ).build()
    }

    override fun refresh(param: CuratedPodcastsParam): Flow<List<CuratedPodcast>> = getStoreStream(
        param
    )
        .map { response ->
            when (response) {
                is StoreReadResponse.Data -> {
                    response.value
                }
                is StoreReadResponse.Error -> {
                    throw response.exception()
                }

                else -> {
                    emptyList()
                }
            }
        }

    override fun refreshSection(param: CuratedPodcastsParam): Flow<SectionState<List<CuratedPodcast>>> {
        var lastKnownGoodPage: List<CuratedPodcast>? = null
        return getStoreStream(param)
            .map { response ->
                when (response) {
                    is StoreReadResponse.Loading ->
                        lastKnownGoodPage?.let { SectionState.Success(it, isRefreshing = true) }
                            ?: SectionState.Loading

                    is StoreReadResponse.Data -> {
                        val page = response.value
                        lastKnownGoodPage = page
                        SectionState.Success(page)
                    }

                    is StoreReadResponse.NoNewData ->
                        lastKnownGoodPage?.let { SectionState.Success(it) } ?: SectionState.Loading

                    is StoreReadResponse.Error ->
                        SectionState.Error(
                            response.exception().type,
                            cachedData = lastKnownGoodPage
                        )

                    else -> SectionState.Loading // exhaustiveness fallback if StoreReadResponse has more cases than assumed above
                }
            }
    }

    private fun getStoreStream(param: CuratedPodcastsParam): Flow<StoreReadResponse<List<CuratedPodcast>>> =
        store.stream(StoreReadRequest.cached(param, param.forceRefresh))

    override fun observePodcasts(param: CuratedPodcastsParam): Flow<List<CuratedPodcast>> =
        curatedPodcastDAO.getCuratedPodcasts()
            .map { sectionedPodcast ->
                sectionedPodcast.groupBy { it.id }
                    .map { (sectionId, podcasts) ->
                        val section = podcasts.first { it.id == sectionId }
                        CuratedPodcast(
                            id = sectionId,
                            title = section.sectionTitle,
                            description = section.sectionDescription,
                            podcasts = mapPodcasts(podcasts)
                        )
                    }
            }.flowOn(dispatcher.computation)


    private suspend fun needsRefresh(podcasts: List<CuratedPodcast>): Boolean {
        if (podcasts.isEmpty()) return false
        return withContext(dispatcher.io) {
            lastSyncDAO.isRequestValid(
                requestType = SyncRequest.CURATED_PODCASTS,
                threshold = 90.minutes,
            )
        }
    }

    private suspend fun delete(param: CuratedPodcastsParam) = withContext(dispatcher.io) {
        transactionRunner {
            curatedPodcastDAO.deletePage(param.page)
        }
    }

    private fun deleteAll() {
//        TODO need to handle eviction logic
    }

    private fun updateCuratedPodcasts(
        dto: List<SectionPodcastDTO>,
        param: CuratedPodcastsParam
    ) {
        val (sectionEntities, podcastEntities) = dto.toSectionEntities()
        val podcasts = dto.mapNotNull {
            it.podcasts
        }.flatten()
            .map {
                PodcastEntity(
                    id = it.id!!,
                    title = it.title ?: "",
                    thumbnail = it.thumbnail ?: "",
                    image = it.image ?: "",
                    description = "",
                    publisher = it.publisher ?: "",
                    genres = ""
                )
            }
        transactionRunner {
            if (param.page == 1) {
                lastSyncDAO.insertLastSync(SyncRequest.CURATED_PODCASTS)
            }
            curatedPodcastDAO.deletePage(param.page)
            curatedPodcastDAO.insertCuratedPodcasts(sectionEntities, podcastEntities)
            podcastDAO.insertPodcasts(podcasts)
        }
    }

    private fun List<SectionPodcastDTO>.toSectionEntities(): Pair<List<CuratedSectionEntity>, List<CuratedPodcastEntity>> {
        val sectionEntities = this.map { section ->
            CuratedSectionEntity(
                id = section.id!!,
                title = section.title.orEmpty(),
                description = section.description.orEmpty(),
                page = 1
            )
        }
        val podcastEntities = this.mapNotNull { section ->
            section.podcasts?.map { podcast ->
                CuratedPodcastEntity(
                    id = "${section.id!!}-${podcast.id!!}",
                    podcast_id = podcast.id!!,
                    section_id = section.id!!
                )
            }
        }.flatten()
        return sectionEntities to podcastEntities
    }

    private fun mapPodcasts(podcasts: List<CuratedSectionWithPodcast>): List<SectionPodcast> {
        return podcasts.map { podcast ->
            SectionPodcast(
                id = podcast.podcastId,
                thumbnail = podcast.thumbnail.orEmpty(),
                image = podcast.image.orEmpty(),
                title = podcast.podcastTitle,
                publisher = podcast.publisher
            )
        }
    }


}