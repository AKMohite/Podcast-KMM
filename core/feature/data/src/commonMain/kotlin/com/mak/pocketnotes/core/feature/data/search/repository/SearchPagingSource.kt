package com.mak.pocketnotes.core.feature.data.search.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mak.pocketnotes.core.feature.data.home.PodcastMapper
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.core.remote.utils.RemoteResult

internal class SearchPagingSource(
  private val api: PocketNotesAPI,
  private val mapper: PodcastMapper,
  private val query: String
) : PagingSource<Int, Podcast>() {

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Podcast> {
    val offset = params.key ?: 0
    val queries = mapOf(
      "q" to query,
      "type" to "podcast",
      "sort_by" to "recent",
      "offset" to offset.toString(),
      "limit" to params.loadSize.toString()
    )

    return when (val response = api.searchPodcasts(queries)) {
      is RemoteResult.Success -> {
        val data = response.data
        val podcasts = mapper.getSearchPodcastResults(data.results)
        val nextKey = if (podcasts.isEmpty() || data.nextOffset == offset) {
          null
        } else {
          data.nextOffset
        }
        LoadResult.Page(
          data = podcasts,
          prevKey = if (offset == 0) null else offset - params.loadSize,
          nextKey = nextKey
        )
      }

      is RemoteResult.Failure -> LoadResult.Error(Exception(response.error.name))
    }
  }

  override fun getRefreshKey(state: PagingState<Int, Podcast>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
    }
  }
}
