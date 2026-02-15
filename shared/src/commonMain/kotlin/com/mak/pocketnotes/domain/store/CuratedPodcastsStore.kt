package com.mak.pocketnotes.domain.store

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.remote.dto.CuratedPodcastDTO
import com.mak.pocketnotes.data.remote.dto.SectionPodcastDTO
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.SectionPodcast
import com.mak.pocketnotes.domain.models.SyncRequest
import com.mak.pocketnotes.local.CuratedSectionWithPodcast
import com.mak.pocketnotes.local.database.DatabaseTransactionRunner
import com.mak.pocketnotes.local.database.dao.CuratedPodcastEntity
import com.mak.pocketnotes.local.database.dao.CuratedSectionEntity
import com.mak.pocketnotes.local.database.dao.ICuratedPodcastDAO
import com.mak.pocketnotes.local.database.dao.ILastSyncDAO
import com.mak.pocketnotes.local.database.dao.IPodcastDAO
import com.mak.pocketnotes.local.database.dao.PodcastEntity
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.Validator
import kotlin.time.Duration.Companion.minutes

class CuratedPodcastsStore: KoinComponent {

    private val api: IPocketNotesAPI by inject()
    private val dao: ICuratedPodcastDAO by inject()
    private val dispatcher: Dispatcher by inject()
    private val transactionRunner: DatabaseTransactionRunner by inject()
    private val lastSyncDAO: ILastSyncDAO by inject()
    private val curatedPodcastDAO: ICuratedPodcastDAO by inject()
    private val podcastDAO: IPodcastDAO by inject()

    operator fun invoke(query: GetCuratedPodcastsQuery = GetCuratedPodcastsQuery()) = StoreBuilder
        .from<Int, List<SectionPodcastDTO>, List<CuratedPodcast>>(
            fetcher = Fetcher.of {  page ->
                api.getCuratedPodcasts(page).curatedLists ?: emptyList()
            },
            sourceOfTruth = SourceOfTruth.of<Int, List<SectionPodcastDTO>, List<CuratedPodcast>>(
                reader = {
                    dao.getCuratedPodcasts()
                        .map { sectionedPodcast ->
                            sectionedPodcast.groupBy { it.id }
                                .map { (sectionId, podcasts) ->
                                    val section = podcasts.first { it.id == sectionId }
                                    CuratedPodcast(
                                        id = sectionId,
                                        title = section.sectionTitle,
                                        podcasts = mapPodcasts(podcasts)
                                    )
                                }
                        }.flowOn(dispatcher.computation)

                },
                writer = { page, dto ->
                    updateLocal(page, dto)
                },
//                deleteAll = {},
                delete = { page ->
                    withContext(dispatcher.io) {
                        transactionRunner {
                            curatedPodcastDAO.deletePage(page)
                        }
                    }
                }
            )
        ).validator(
            Validator.by {
                if (it.isEmpty()) return@by false
                withContext(dispatcher.io) {
                    lastSyncDAO.isRequestValid(
                        requestType = SyncRequest.CURATED_PODCASTS,
                        threshold = 90.minutes,
                    )
                }
            }
        ).build()
        .stream(StoreReadRequest.cached(query.page, false))
        .map { response ->
            when (response) {
                is StoreReadResponse.Data -> {
                    response.value
                }
                else -> {
                    emptyList()
                }
            }
        }

    private suspend fun updateLocal(page: Int, dto: List<SectionPodcastDTO>) = withContext(dispatcher.io) {
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
            if (page == 1) {
                lastSyncDAO.insertLastSync(SyncRequest.CURATED_PODCASTS)
            }
            curatedPodcastDAO.deletePage(page)
            curatedPodcastDAO.insertCuratedPodcasts(sectionEntities, podcastEntities)
            podcastDAO.insertPodcasts(podcasts)
        }
    }


    private fun mapPodcasts(podcasts: List<CuratedSectionWithPodcast>): List<SectionPodcast> {
        return podcasts.map { podcast ->
            SectionPodcast(
                id = podcast.podcastId,
                image = podcast.thumbnail ?: "",
                title = podcast.podcastTitle,
                publisher = podcast.publisher ?: ""
            )
        }
    }
}

private fun List<SectionPodcastDTO>.toSectionEntities(): Pair<List<CuratedSectionEntity>, List<CuratedPodcastEntity>> {
    val sectionEntities = this.map { section ->
        CuratedSectionEntity(
            id = section.id!!,
            title = section.title ?: "",
            page = 1
        )
    }
    val podcastEntities = this.mapNotNull { section ->
        section.podcasts?.map { podcast ->
            CuratedPodcastEntity(
                id = "${section.id!!}-${podcast.id!!}",
                podcast_id = podcast.id,
                section_id = section.id
            )
        }
    }.flatten()
    return sectionEntities to podcastEntities
}

private fun List<SectionPodcastDTO>.toCuratedPodcasts(): List<CuratedPodcast> {
    return this.map {section ->
        val podcasts = getPodcasts(section.podcasts ?: emptyList())
        CuratedPodcast(
            id = section.id!!,
            title = section.title ?: "",
            podcasts = podcasts
        )
    }
}

private fun getPodcasts(curatedPodcastDTOS: List<CuratedPodcastDTO>): List<SectionPodcast> {
    return curatedPodcastDTOS.map { podcast ->
        SectionPodcast(
            id = podcast.id!!,
            title = podcast.title ?: "",
            image = podcast.thumbnail ?: "",
            publisher = podcast.publisher ?: ""
        )
    }
}

data class GetCuratedPodcastsQuery(
    val page: Int = 1,
    val forceRefresh: Boolean = false
)
