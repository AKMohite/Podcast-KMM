package com.mak.pocketnotes.domain.store

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.common.models.SyncRequest
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.EpisodeDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO.Companion.DEFAULT_ID
import com.mak.pocketnotes.core.feature.data.home.PodcastMapper
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.core.remote.dto.PodcastDTO
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
import kotlin.time.Instant

/**
 * Pair of Podcast id and next episode date
 */
typealias EpisodeParams = Pair<String, Long>

internal class EpisodeStore: KoinComponent {

    private val api: PocketNotesAPI by inject()
    private val transactionRunner: DatabaseTransactionRunner by inject()
    private val episodeDAO: EpisodeDAO by inject()
    private val lastSyncDAO: LastSyncDAO by inject()
    private val dispatcher: DispatcherProvider by inject()
    private val mapper: PodcastMapper by inject()

    operator fun invoke() = StoreBuilder
        .from<EpisodeParams, PodcastDTO, List<PodcastEpisode>>(
            fetcher = Fetcher.of { (podcastId, nextEpisodeDate) ->
                val query = mapOf(
                    "next_episode_pub_date" to (nextEpisodeDate.toString())
                )
                api.getPodcastDetails(podcastId, queryMap = query)
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { (podcastId, nextEpisodeDate) ->
                    episodeDAO.getEpisodes(podcastId = podcastId, nextEpisodeDate = nextEpisodeDate.let { Instant.fromEpochMilliseconds(it) })
                        .map { mapper.episodeEntityToModels(it) }
                        .flowOn(dispatcher.computation)
                },
                writer = { (podcastId, _), dto ->
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
                },
                deleteAll = {
//                    withContext(dispatcher.io) {
//                        episodeDAO.removeEpisodes()
//                    }
                },
                delete = { (podcastId, nextDate) ->
                    withContext(dispatcher.io) {
                        episodeDAO.removeEpisodes(podcastId, Instant.fromEpochMilliseconds(nextDate))
                    }
                }
            )
        )
        .validator(
            Validator.by { episodes ->
                withContext(dispatcher.io) {
                    lastSyncDAO.isRequestValid(
                        requestType = SyncRequest.PODCAST_EPISODES,
                        threshold = 1.days,
                        entityId = episodes.firstOrNull()?.podcastId ?: DEFAULT_ID
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