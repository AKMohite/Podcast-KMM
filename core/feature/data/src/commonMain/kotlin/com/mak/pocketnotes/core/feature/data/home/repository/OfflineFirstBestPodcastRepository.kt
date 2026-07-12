package com.mak.pocketnotes.core.feature.data.home.repository

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.common.models.SyncRequest
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.database.dao.PodcastEntity
import com.mak.pocketnotes.core.database.dao.TrendingPodcastDAO
import com.mak.pocketnotes.core.database.dao.TrendingPodcastEntity
import com.mak.pocketnotes.core.feature.data.home.PodcastMapper
import com.mak.pocketnotes.core.feature.domain.home.models.BestQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.core.remote.dto.BestPodcastDTO
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

internal class OfflineFirstBestPodcastRepository(
    private val api: PocketNotesAPI,
    private val transactionRunner: DatabaseTransactionRunner,
    private val podcastDAO: PodcastDAO,
    private val trendingPodcastDAO: TrendingPodcastDAO,
    private val lastSyncDAO: LastSyncDAO,
    private val mapper: PodcastMapper,
    private val dispatcher: DispatcherProvider
) : BestPodcastRepository {

    private val store by lazy {
        StoreBuilder
            .from<BestQueryParam, BestPodcastDTO, List<Podcast>>(
                fetcher = Fetcher.of { param ->
                    fetchPodcastsFromRemote(param)
                },
                sourceOfTruth = SourceOfTruth.of(
                    reader = { param ->
                        observePodcasts(param)
                    },
                    writer = { param, dto ->
                        updatePodcasts(dto, param)
                    },
                    deleteAll = {
                        deleteAll()
                    },
                    delete = { param ->
                        delete(param)
                    },
                )
            ).validator(
                Validator.by { podcasts ->
                    return@by needsRefresh(podcasts)
                }
            )
            .build()
    }

    override fun refresh(param: BestQueryParam): Flow<List<Podcast>> = store
        .stream(StoreReadRequest.cached(param, param.forceRefresh))
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

    private suspend fun needsRefresh(podcasts: List<Podcast>): Boolean {
        if (podcasts.isEmpty()) {
            return false
        }
        return withContext(dispatcher.io) {
            lastSyncDAO.isRequestValid(
                requestType = SyncRequest.BEST_PODCASTS,
                threshold = 90.minutes,
            )
        }
    }

    private suspend fun updatePodcasts(
        dto: BestPodcastDTO,
        param: BestQueryParam
    ) {
        val podcasts = mapper.jsonToEntities(dto.podcasts ?: emptyList())
        val trendingPodcasts = podcasts.map {
            TrendingPodcastEntity(
                id = 0L,
                podcast_id = it.id,
                page = param.page
            )
        }
        updateLocal(param.page, podcasts, trendingPodcasts)
    }

    override fun observePodcasts(param: BestQueryParam): Flow<List<Podcast>> =
        trendingPodcastDAO.getBestPodcasts(param.page)
            .map { entities -> mapper.entityToModels(entities) }
            .flowOn(dispatcher.computation)

    private suspend fun fetchPodcastsFromRemote(param: BestQueryParam): BestPodcastDTO {
        val queryMap = mutableMapOf(
            "page" to param.page.toString()
        )
        param.genreId?.let { id ->
            queryMap["genre_id"] = id.toString()
        }
        return api.getBestPodcasts(queryMap)
    }

    private suspend fun delete(param: BestQueryParam) {
        withContext(dispatcher.io) {
            transactionRunner {
                trendingPodcastDAO.deletePage(page = param.page)
            }
        }
    }

    private suspend fun deleteAll() {
        withContext(dispatcher.io) {
            transactionRunner(trendingPodcastDAO::deleteAll)
        }
    }

    private suspend fun updateLocal(
        page: Int,
        podcasts: List<PodcastEntity>,
        trendingPodcasts: List<TrendingPodcastEntity>
    ) = withContext(dispatcher.io) {
        transactionRunner {
            if (page == 1) {
                lastSyncDAO.insertLastSync(SyncRequest.BEST_PODCASTS)
            }
            podcastDAO.insertPodcasts(podcasts)
            trendingPodcastDAO.deletePage(page)
            trendingPodcastDAO.upsertPage(trendingPodcasts)
        }
    }

}