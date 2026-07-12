package com.mak.pocketnotes.core.feature.data.podcastdetails.repository

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.common.models.SyncRequest
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.database.dao.RelatedPodcastDAO
import com.mak.pocketnotes.core.feature.data.home.PodcastMapper
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.RelatedPodcastRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.RelatedPodcasts
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.core.remote.dto.PodcastDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.Validator
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

internal class OfflineFirstRelatedPodcastRepository(
    private val api: PocketNotesAPI,
    private val transactionRunner: DatabaseTransactionRunner,
    private val podcastDAO: PodcastDAO,
    private val relatedPodcastDAO: RelatedPodcastDAO,
    private val lastSyncDAO: LastSyncDAO,
    private val dispatcher: DispatcherProvider,
    private val mapper: PodcastMapper
) : RelatedPodcastRepository {

    private val store by lazy {
        StoreBuilder
            .from<String, List<PodcastDTO>, RelatedPodcasts>(
                fetcher = Fetcher.of { podcastId ->
                    fetch(podcastId)
                },
                sourceOfTruth = SourceOfTruth.of(
                    reader = { podcastId ->
                        observe(podcastId)
                    },
                    writer = { podcastId, dto ->
                        update(podcastId, dto)
                    },
                    deleteAll = {
                        deleteAll()
                    },
                    delete = { podcastId ->
                        delete(podcastId)
                    }
                )
            ).validator(
                Validator.by { relatedPodcasts ->
                    needsRefresh(relatedPodcasts)
                }
            ).build()
    }

    override fun refresh(podcastId: String): Flow<RelatedPodcasts> = store
        .stream(StoreReadRequest.cached(key = podcastId, refresh = false))
        .filterIsInstance<StoreReadResponse.Data<RelatedPodcasts>>()
        .map { storeResponse ->
            storeResponse.requireData()
        }.flowOn(dispatcher.io)

    override fun observe(podcastId: String): Flow<RelatedPodcasts> =
        relatedPodcastDAO.getPodcast(podcastId)
            .map {
                RelatedPodcasts(
                    podcastId,
                    mapper.entityToModels(it)
                )
            }
            .flowOn(dispatcher.computation)

    private suspend fun needsRefresh(relatedPodcasts: RelatedPodcasts): Boolean =
        withContext(dispatcher.io) {
            if (relatedPodcasts.related.isEmpty()) return@withContext true
            lastSyncDAO.isRequestValid(
                requestType = SyncRequest.PODCAST_RECOMMENDATIONS,
                threshold = if (relatedPodcasts.related.isNotEmpty()) 4.days else 1.hours,
                entityId = relatedPodcasts.podcastId
            )
        }

    private suspend fun delete(podcastId: String) {
        withContext(dispatcher.io) {
            relatedPodcastDAO.removePodcast(podcastId)
        }
    }

    private suspend fun deleteAll() {
        withContext(dispatcher.io) {
            relatedPodcastDAO.removePodcasts()
        }
    }

    private suspend fun update(
        podcastId: String,
        dto: List<PodcastDTO>
    ) {
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
    }

    private suspend fun fetch(podcastId: String): List<PodcastDTO> =
        api.getPodcastRecommendations(podcastId).recommendations ?: emptyList()

}