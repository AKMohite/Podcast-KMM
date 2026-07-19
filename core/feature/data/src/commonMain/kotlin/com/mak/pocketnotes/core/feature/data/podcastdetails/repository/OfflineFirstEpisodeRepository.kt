package com.mak.pocketnotes.core.feature.data.podcastdetails.repository

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.common.models.SyncRequest
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.EpisodeDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO.Companion.DEFAULT_ID
import com.mak.pocketnotes.core.feature.data.home.PodcastMapper
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.EpisodeParams
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.EpisodeRepository
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
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class OfflineFirstEpisodeRepository(
    private val api: PocketNotesAPI,
    private val transactionRunner: DatabaseTransactionRunner,
    private val episodeDAO: EpisodeDAO,
    private val lastSyncDAO: LastSyncDAO,
    private val dispatcher: DispatcherProvider,
    private val mapper: PodcastMapper
) : EpisodeRepository {

    private val store by lazy {
        StoreBuilder
            .from<EpisodeParams, PodcastDTO, List<PodcastEpisode>>(
                fetcher = Fetcher.of { (podcastId, nextEpisodeDate) ->
                    fetchEpisodes(nextEpisodeDate, podcastId)
                },
                sourceOfTruth = SourceOfTruth.of(
                    reader = { (podcastId, nextEpisodeDate) ->
                        observeEpisodes(podcastId, nextEpisodeDate)
                    },
                    writer = { (podcastId, _), dto ->
                        update(dto, podcastId)
                    },
                    deleteAll = {
//                    withContext(dispatcher.io) {
//                        episodeDAO.removeEpisodes()
//                    }
                    },
                    delete = { (podcastId, nextDate) ->
                        delete(podcastId, nextDate)
                    }
                )
            )
            .validator(
                Validator.by { episodes ->
                    isDataFresh(episodes)
                }
            ).build()
    }

    override fun observeEpisodes(params: EpisodeParams): Flow<List<PodcastEpisode>> =
        observeEpisodes(params.first, params.second)

    override fun refresh(params: EpisodeParams): Flow<List<PodcastEpisode>> = store
        .stream(StoreReadRequest.cached(key = params, refresh = false))
        .filterIsInstance<StoreReadResponse.Data<List<PodcastEpisode>>>()
        .map { response ->
            response.requireData()
        }
        .flowOn(dispatcher.io)

    private suspend fun isDataFresh(episodes: List<PodcastEpisode>): Boolean =
        withContext(dispatcher.io) {
            if (episodes.isEmpty()) return@withContext false
            lastSyncDAO.isRequestValid(
                requestType = SyncRequest.PODCAST_EPISODES,
                threshold = 1.days,
                entityId = episodes.firstOrNull()?.podcastId ?: DEFAULT_ID
            )
        }

    private suspend fun delete(podcastId: String, nextDate: Long?) {
        withContext(dispatcher.io) {
            val nextDate = nextDate?.let {
                Instant.fromEpochMilliseconds(it)
            } ?: Instant.DISTANT_PAST
            episodeDAO.removeEpisodes(podcastId, nextDate)
        }
    }

    private suspend fun update(
        dto: PodcastDTO,
        podcastId: String
    ) {
        withContext(dispatcher.io) {
            transactionRunner {
                val episodes = mapper.mapEpisodeEntities(
                    dto.episodes,
                    podcastId,
                    dto.nextEpisodeDate
                )
                episodeDAO.insertEpisodes(episodes)
            }
        }
    }

    private fun observeEpisodes(
        podcastId: String,
        nextEpisodeDate: Long?
    ): Flow<List<PodcastEpisode>> = episodeDAO.getEpisodes(
        podcastId = podcastId,
        nextEpisodeDate = nextEpisodeDate?.let { Instant.fromEpochMilliseconds(it) }
            ?: Clock.System.now())
        .map { mapper.episodeEntityToModels(it) }
        .flowOn(dispatcher.computation)

    private suspend fun fetchEpisodes(
        nextEpisodeDate: Long?,
        podcastId: String
    ): PodcastDTO {
        val query = mutableMapOf<String, String>()
        nextEpisodeDate?.let {
            query["next_episode_pub_date"] = it.toString()
        }
        return api.getPodcastDetails(podcastId, queryMap = query).getOrThrow()
    }

}