package com.mak.pocketnotes.domain.store

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.common.models.SyncRequest
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.CuratedPodcastDAO
import com.mak.pocketnotes.core.database.dao.CuratedPodcastEntity
import com.mak.pocketnotes.core.database.dao.CuratedSectionEntity
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.database.dao.PodcastEntity
import com.mak.pocketnotes.core.database.queries.CuratedSectionWithPodcast
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.core.remote.dto.SectionPodcastDTO
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.SectionPodcast
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

    private val api: PocketNotesAPI by inject()
    private val dao: CuratedPodcastDAO by inject()
    private val dispatcher: DispatcherProvider by inject()
    private val transactionRunner: DatabaseTransactionRunner by inject()
    private val lastSyncDAO: LastSyncDAO by inject()
    private val curatedPodcastDAO: CuratedPodcastDAO by inject()
    private val podcastDAO: PodcastDAO by inject()

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
                                        description = section.sectionDescription,
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
                thumbnail = podcast.thumbnail.orEmpty(),
                image = podcast.image.orEmpty(),
                title = podcast.podcastTitle,
                publisher = podcast.publisher
            )
        }
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

data class GetCuratedPodcastsQuery(
    val page: Int = 1,
    val forceRefresh: Boolean = false
)
