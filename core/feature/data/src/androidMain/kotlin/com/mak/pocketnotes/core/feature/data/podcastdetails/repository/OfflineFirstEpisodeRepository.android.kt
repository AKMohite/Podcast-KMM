package com.mak.pocketnotes.core.feature.data.podcastdetails.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.cash.sqldelight.paging3.QueryPagingSource
import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.EpisodeDAO
import com.mak.pocketnotes.core.database.dao.EpisodeEntity
import com.mak.pocketnotes.core.database.dao.EpisodePagingKeysDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.feature.data.home.PodcastMapper
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import kotlin.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
internal actual fun createEpisodePager(
  podcastId: String,
  api: PocketNotesAPI,
  episodeDAO: EpisodeDAO,
  pagingKeysDAO: EpisodePagingKeysDAO,
  lastSyncDAO: LastSyncDAO,
  transactionRunner: DatabaseTransactionRunner,
  mapper: PodcastMapper,
  dispatcher: DispatcherProvider
): Flow<PagingData<PodcastEpisode>> {
  return Pager(
    config = PagingConfig(
      pageSize = 10,
      enablePlaceholders = false
    ),
    remoteMediator = EpisodeRemoteMediator(
      podcastId = podcastId,
      api = api,
      episodeDAO = episodeDAO,
      pagingKeysDAO = pagingKeysDAO,
      lastSyncDAO = lastSyncDAO,
      transactionRunner = transactionRunner,
      mapper = mapper
    ),
    pagingSourceFactory = {
      QueryPagingSource(
        countQuery = episodeDAO.countEpisodes(podcastId),
        transacter = episodeDAO.getTransacter(),
        context = dispatcher.io,
        queryProvider = { limit, offset ->
          episodeDAO.getEpisodesPaginated(podcastId, limit, offset)
        }
      )
    }
  ).flow.map { pagingData ->
    pagingData.map { mapper.episodeEntityToModel(it) }
  }
}

@OptIn(ExperimentalPagingApi::class)
internal actual fun createEpisodePagerV2(
  podcastId: String,
  api: PocketNotesAPI,
  episodeDAO: EpisodeDAO,
  pagingKeysDAO: EpisodePagingKeysDAO,
  lastSyncDAO: LastSyncDAO,
  transactionRunner: DatabaseTransactionRunner,
  mapper: PodcastMapper,
  dispatcher: DispatcherProvider
): Flow<PagingData<PodcastEpisode>> {
  return Pager(
    config = PagingConfig(
      pageSize = 10,
      initialLoadSize = 10,
      enablePlaceholders = false
    ),
    remoteMediator = EpisodeKeysetRemoteMediator(
      podcastId = podcastId,
      api = api,
      episodeDAO = episodeDAO,
      pagingKeysDAO = pagingKeysDAO,
      lastSyncDAO = lastSyncDAO,
      transactionRunner = transactionRunner,
      mapper = mapper
    ),
    pagingSourceFactory = {
//      EpisodeKeysetPagingSource(
//        episodeDAO = episodeDAO,
//        podcastId = podcastId,
//        dispatcher = dispatcher
//      )
      QueryPagingSource<Instant, EpisodeEntity>(
        transacter = episodeDAO.getTransacter(),
        context = dispatcher.io,
        pageBoundariesProvider = { anchor, limit ->
          episodeDAO.getEpisodePageBoundaries(podcastId, anchor, limit)
        },
        queryProvider = { beginInclusive, endExclusive ->
          episodeDAO.getEpisodesByBoundary(podcastId, beginInclusive, endExclusive)
        }
      )
    }
  ).flow.map { pagingData ->
    pagingData.map { mapper.episodeEntityToModel(it) }
  }
}
