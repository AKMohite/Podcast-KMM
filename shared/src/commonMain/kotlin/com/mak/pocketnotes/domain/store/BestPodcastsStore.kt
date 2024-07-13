package com.mak.pocketnotes.domain.store

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.remote.dto.BestPodcastDTO
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.mapper.PodcastMapper
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.models.SyncRequest
import com.mak.pocketnotes.local.database.DatabaseTransactionRunner
import com.mak.pocketnotes.local.database.dao.ILastSyncDAO
import com.mak.pocketnotes.local.database.dao.IPodcastDAO
import com.mak.pocketnotes.local.database.dao.ITrendingPodcastDAO
import com.mak.pocketnotes.local.database.dao.PodcastEntity
import com.mak.pocketnotes.local.database.dao.TrendingPodcastEntity
import kotlinx.coroutines.flow.Flow
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

class BestPodcastsStore: KoinComponent {
    private val api: IPocketNotesAPI by inject()
    private val transactionRunner: DatabaseTransactionRunner by inject()
    private val podcastDAO: IPodcastDAO by inject()
    private val trendingPodcastDAO: ITrendingPodcastDAO by inject()
    private val lastSyncDAO: ILastSyncDAO by inject()
    private val mapper: PodcastMapper by inject()
    private val dispatcher: Dispatcher by inject()

    operator fun invoke(query: GetGenreQuery = GetGenreQuery()): Flow<List<Podcast>> = StoreBuilder
        .from<Unit, BestPodcastDTO, List<Podcast>>(
            fetcher = Fetcher.of<Unit, BestPodcastDTO> {
                val queryMap = mutableMapOf(
                    "page" to query.page.toString()
                )
                query.genreId?.let { id ->
                    queryMap["genre_id"] = id.toString()
                }
                api.getBestPodcasts(queryMap)
            },
            sourceOfTruth = SourceOfTruth.of<Unit, BestPodcastDTO, List<Podcast>>(
                reader = {
                    trendingPodcastDAO.getBestPodcasts()
                        .map { entities -> mapper.entityToModels(entities) }
                        .flowOn(dispatcher.computation)
                },
                writer = { _, dto ->
                    val podcasts = mapper.jsonToEntities(dto.podcasts ?: emptyList())
                    val trendingPodcasts = podcasts.map {
                        TrendingPodcastEntity(
                            id = 0L,
                            podcast_id = it.id,
                            page = query.page
                        )
                    }
                    updateLocal(query.page, podcasts, trendingPodcasts)
                },
                deleteAll = {
                    withContext(dispatcher.io) {
                        transactionRunner(trendingPodcastDAO::deleteAll)
                    }
                },
                delete = {
                    withContext(dispatcher.io) {
                        transactionRunner {
                            trendingPodcastDAO.deletePage(page = query.page)
                        }
                    }
                },
            )
        ).validator(
            Validator.by {
                withContext(dispatcher.io) {
                    lastSyncDAO.isRequestValid(
                        requestType = SyncRequest.BEST_PODCASTS,
                        threshold = if (it.isNotEmpty()) 90.minutes else 5.minutes,
                    )
                }
            }
        ).build()
        .stream(StoreReadRequest.cached(Unit, false))
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

    private suspend fun updateLocal(
        page: Int,
        podcasts: List<PodcastEntity>,
        trendingPodcasts: List<TrendingPodcastEntity>
    ) = withContext(dispatcher.io) {
        transactionRunner {
            lastSyncDAO.insertLastSync(SyncRequest.BEST_PODCASTS)
            podcastDAO.insertPodcasts(podcasts)
            trendingPodcastDAO.deletePage(page)
            trendingPodcastDAO.upsertPage(trendingPodcasts)
        }
    }
}

data class GetGenreQuery(
    val genreId: Int? = null,
    val page: Int = 1
)