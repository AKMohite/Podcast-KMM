package com.mak.pocketnotes.core.feature.data.podcastdetails.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mak.pocketnotes.core.common.models.SyncRequest
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.EpisodeDAO
import com.mak.pocketnotes.core.database.dao.EpisodeEntity
import com.mak.pocketnotes.core.database.dao.EpisodePagingKeysDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.feature.data.home.PodcastMapper
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

@OptIn(ExperimentalPagingApi::class)
class EpisodeKeysetRemoteMediator(
  private val podcastId: String,
  private val api: PocketNotesAPI,
  private val episodeDAO: EpisodeDAO,
  private val pagingKeysDAO: EpisodePagingKeysDAO,
  private val lastSyncDAO: LastSyncDAO,
  private val transactionRunner: DatabaseTransactionRunner,
  private val mapper: PodcastMapper
) : RemoteMediator<Instant, EpisodeEntity>() {

  override suspend fun initialize(): InitializeAction {
    val isFresh = lastSyncDAO.isRequestValid(
      requestType = SyncRequest.PODCAST_EPISODES,
      entityId = podcastId,
      threshold = 1.days
    )

    return if (isFresh) {
      InitializeAction.SKIP_INITIAL_REFRESH
    } else {
      InitializeAction.LAUNCH_INITIAL_REFRESH
    }
  }

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Instant, EpisodeEntity>
  ): MediatorResult {
    return try {
      val nextEpisodeDate: Instant? = when (loadType) {
        LoadType.REFRESH -> null
        LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
        LoadType.APPEND -> {
          val remoteKeys =
            pagingKeysDAO.getNextEpisodeDate(podcastId) ?: return MediatorResult.Success(
              endOfPaginationReached = true
            )
          remoteKeys
        }
      }

      val query = mutableMapOf<String, String>()
      nextEpisodeDate?.let {
        query["next_episode_pub_date"] = it.toEpochMilliseconds().toString()
      }

      val response = api.getPodcastDetails(podcastId, queryMap = query).getOrThrow()
      val episodes = response.episodes ?: emptyList()
      val nextDate = response.nextEpisodeDate

      val endOfPaginationReached = episodes.isEmpty() || nextDate == null

      transactionRunner {
        if (loadType == LoadType.REFRESH) {
          pagingKeysDAO.deleteKey(podcastId)
//          episodeDAO.removeEpisodes(podcastId)
        }

        val entities = mapper.mapEpisodeEntities(episodes, podcastId, nextDate)
        episodeDAO.insertEpisodes(entities)
        pagingKeysDAO.insertKey(
          podcastId,
          nextDate?.let { Instant.fromEpochMilliseconds(it) }
        )
        if (loadType == LoadType.REFRESH) {
          lastSyncDAO.insertLastSync(SyncRequest.PODCAST_EPISODES, podcastId)
        }
      }

      MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
    } catch (e: Exception) {
      MediatorResult.Error(e)
    }
  }
}
