package com.mak.pocketnotes.core.feature.data.podcastdetails.repository

import androidx.paging.PagingData
import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.EpisodeDAO
import com.mak.pocketnotes.core.database.dao.EpisodePagingKeysDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.feature.data.home.PodcastMapper
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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
  // Paging 3 SQLDelight extension is not available for iOS yet in this project setup.
  // Returning empty flow for now.
  return flowOf(PagingData.empty())
}

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
  return flowOf(PagingData.empty())
}
