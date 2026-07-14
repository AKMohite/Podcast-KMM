package com.mak.pocketnotes.core.feature.data.podcastdetails.repository

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.common.models.SyncRequest
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.EpisodeDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.feature.data.home.PodcastMapper
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.PodcastRepository
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

internal class OfflineFirstPodcastRepository(
    private val api: PocketNotesAPI,
    private val transactionRunner: DatabaseTransactionRunner,
    private val podcastDAO: PodcastDAO,
    private val episodeDAO: EpisodeDAO,
    private val lastSyncDAO: LastSyncDAO,
    private val dispatcher: DispatcherProvider,
    private val mapper: PodcastMapper
) : PodcastRepository {

    private val store by lazy {
        StoreBuilder.from<String, PodcastDTO, Podcast>(
            fetcher = Fetcher.Companion.of { podcastId ->
                fetchPodcast(podcastId)
            },
            sourceOfTruth = SourceOfTruth.Companion.of(
                reader = { podcastId ->
                    observePodcast(podcastId)
                },
                writer = { podcastId, dto ->
                    updatePodcast(dto, podcastId)
                },
                deleteAll = {},
                delete = { podcastId ->
                    delete(podcastId)
                }
            )
        ).validator(
            Validator.Companion.by { podcast ->
                return@by needsRefresh(podcast)
            }
        ).build()
    }

    override fun refresh(podcastId: String): Flow<Podcast> = store
        .stream(StoreReadRequest.cached(podcastId, false))
        .filterIsInstance<StoreReadResponse.Data<Podcast>>()
        .map { response ->
            response.requireData()
        }.flowOn(dispatcher.io)

    // TODO podcast can be null
    override fun observePodcast(podcastId: String): Flow<Podcast> = podcastDAO.getPodcast(podcastId)
        .map { podcastEntity ->
            mapper.entityToModel(podcastEntity)
        }.flowOn(dispatcher.computation)

    private suspend fun needsRefresh(podcast: Podcast?): Boolean = withContext(dispatcher.io) {
        if (podcast == null) return@withContext true
        lastSyncDAO.isRequestValid(
            requestType = SyncRequest.PODCAST_DETAILS,
            threshold = 1.days,
            entityId = podcast.id
        )
    }

    private suspend fun delete(podcastId: String) {
        withContext(dispatcher.io) {
            podcastDAO.removePodcast(podcastId)
        }
    }

    private suspend fun updatePodcast(
        dto: PodcastDTO,
        podcastId: String
    ) {
        withContext(dispatcher.io) {
            transactionRunner {
                podcastDAO.insertPodcast(mapper.jsonToEntity(dto))
                val episodes = mapper.mapEpisodeEntities(
                    dto.episodes,
                    podcastId,
                    dto.nextEpisodeDate
                )
                episodeDAO.insertEpisodes(episodes)
                lastSyncDAO.insertLastSync(
                    SyncRequest.PODCAST_DETAILS,
                    podcastId
                )
                lastSyncDAO.insertLastSync(
                    SyncRequest.PODCAST_EPISODES,
                    podcastId
                )
            }
        }
    }

    private suspend fun fetchPodcast(podcastId: String): PodcastDTO = api
        .getPodcastDetails(podcastId).getOrThrow()

}