package com.mak.pocketnotes.domain.store

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.remote.dto.PodcastDTO
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.mapper.PodcastMapper
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.models.SyncRequest
import com.mak.pocketnotes.local.database.DatabaseTransactionRunner
import com.mak.pocketnotes.local.database.dao.ILastSyncDAO
import com.mak.pocketnotes.local.database.dao.IPodcastDAO
import com.mak.pocketnotes.local.database.dao.IRelatedPodcastDAO
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.Validator
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

internal class RelatedPodcastsStore: KoinComponent {

    private val api: IPocketNotesAPI by inject()
    private val podcastDAO: IPodcastDAO by inject()
    private val transactionRunner: DatabaseTransactionRunner by inject()
    private val relatedPodcastDAO: IRelatedPodcastDAO by inject()
    private val lastSyncDAO: ILastSyncDAO by inject()
    private val dispatcher: Dispatcher by inject()
    private val mapper: PodcastMapper by inject()

    operator fun invoke(id: String) = StoreBuilder
        .from<String, List<PodcastDTO>, List<Podcast>>(
            fetcher = Fetcher.of { podcastId ->
                api.getPodcastRecommendations(podcastId).recommendations ?: emptyList()
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { podcastId ->
                    relatedPodcastDAO.getPodcast(podcastId)
                        .map {
                            mapper.entityToModels(it)
                        }
                        .flowOn(dispatcher.computation)
                },
                writer = { podcastId, dto ->
                    withContext(dispatcher.io) {
                        transactionRunner {
                            lastSyncDAO.insertLastSync(
                                SyncRequest.PODCAST_RECOMMENDATIONS,
                                podcastId
                            )
                            relatedPodcastDAO.insertPodcasts(
                                mapper.jsonToRelatedEntities(
                                    podcastId,
                                    dto
                                )
                            )
                            podcastDAO.insertPodcasts(mapper.jsonToEntities(dto))
                        }
                    }
                },
                deleteAll = {
                    withContext(dispatcher.io) {
                        relatedPodcastDAO.removePodcasts()
                    }
                },
                delete = { podcastId ->
                    withContext(dispatcher.io) {
                        relatedPodcastDAO.removePodcast(podcastId)
                    }
                }
            )
        ).validator(
            Validator.by { podcasts ->
                withContext(dispatcher.io) {
                    lastSyncDAO.isRequestValid(
                        requestType = SyncRequest.PODCAST_RECOMMENDATIONS,
                        threshold = if (podcasts.isNotEmpty()) 4.days else 1.hours,
                        entityId = id
                    )
                }
            }
        ).build()
//        .stream(StoreReadRequest.cached(key = id, refresh = false))
//        .filter { storeResponse ->
//            storeResponse is StoreReadResponse.Data
//        }.map { storeResponse ->
//            storeResponse.requireData()
//        }

}