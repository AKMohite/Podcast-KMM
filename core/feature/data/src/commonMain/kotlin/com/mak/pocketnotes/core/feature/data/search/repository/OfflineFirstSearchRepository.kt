package com.mak.pocketnotes.core.feature.data.search.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.feature.data.home.PodcastMapper
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.feature.domain.search.repository.SearchRepository
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.core.remote.utils.RemoteResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class OfflineFirstSearchRepository(
  private val api: PocketNotesAPI,
  private val podcastDAO: PodcastDAO,
  private val mapper: PodcastMapper,
  private val dispatcher: DispatcherProvider
) : SearchRepository {

  override fun searchPodcasts(query: String): Flow<PagingData<Podcast>> {
    return Pager(
      config = PagingConfig(
        pageSize = 10,
        enablePlaceholders = false
      ),
      pagingSourceFactory = {
        SearchPagingSource(api, mapper, query)
      }
    ).flow
  }

  override fun searchPodcastsList(query: String): Flow<List<Podcast>> = flow {
    val queries = mapOf(
      "q" to query,
      "type" to "podcast"
    )
    val response = api.searchPodcasts(queries)
    if (response is RemoteResult.Success) {
      val podcasts = mapper.getSearchPodcastResults(response.data.results)
      emit(podcasts)
    } else {
      emit(emptyList())
    }
  }.flowOn(dispatcher.io)

  override fun searchEpisodes(query: String): Flow<List<PodcastEpisode>> = flow {
    val response = api.searchEpisodes(
      mapOf(
        "q" to query,
        "type" to "episode"
      )
    )
    if (response is RemoteResult.Success) {
      val episodes = mapper.getPodcastEpisodes(response.data.results, "")
      emit(episodes)
    } else {
      emit(emptyList())
    }
  }.flowOn(dispatcher.io)

  override fun getLocalSuggestions(query: String): Flow<List<Podcast>> {
    val sanitizedQuery = query.trim()
    if (sanitizedQuery.isEmpty()) return flow { emit(emptyList()) }

    return podcastDAO.searchPodcasts(sanitizedQuery)
      .map { entities ->
        entities.map { mapper.entityToModel(it) }
      }.flowOn(dispatcher.computation)
  }
}
