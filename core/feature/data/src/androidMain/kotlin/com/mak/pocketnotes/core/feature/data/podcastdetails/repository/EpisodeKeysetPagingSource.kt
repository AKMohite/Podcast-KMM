package com.mak.pocketnotes.core.feature.data.podcastdetails.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.cash.sqldelight.Query
import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.dao.EpisodeDAO
import com.mak.pocketnotes.core.database.dao.EpisodeEntity
import kotlin.time.Instant
import kotlinx.coroutines.withContext

// check QueryPagingSource with pageBoundaries
class EpisodeKeysetPagingSource(
  private val episodeDAO: EpisodeDAO,
  private val podcastId: String,
  private val dispatcher: DispatcherProvider
) : PagingSource<Instant, EpisodeEntity>() {

  private val listener = Query.Listener {
    invalidate()
  }

  init {
    episodeDAO.getEpisodesQuery(podcastId).addListener(listener)
    registerInvalidatedCallback {
      episodeDAO.getEpisodesQuery(podcastId).removeListener(listener)
    }
  }

  override suspend fun load(params: LoadParams<Instant>): LoadResult<Instant, EpisodeEntity> {
    return withContext(dispatcher.io) {
      try {
        val lastTimestamp = params.key
        val limit = params.loadSize.toLong()

        val query = if (lastTimestamp == null) {
          episodeDAO.getEpisodesInitial(podcastId, limit)
        } else {
          episodeDAO.getEpisodesKeyset(podcastId, lastTimestamp, limit)
        }

        val data = query.executeAsList()
        val nextKey = data.lastOrNull()?.published_on

        LoadResult.Page(
          data = data,
          prevKey = null, // Only forward paging for now
          nextKey = if (data.size < params.loadSize) null else nextKey
        )
      } catch (e: Exception) {
        LoadResult.Error(e)
      }
    }
  }

  override fun getRefreshKey(state: PagingState<Instant, EpisodeEntity>): Instant? {
    return state.anchorPosition?.let { anchorPosition ->
      state.closestItemToPosition(anchorPosition)?.published_on
    }
  }
}
