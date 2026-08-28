package com.mak.pocketnotes.core.feature.data.search.repository

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

  override fun searchPodcasts(query: String): Flow<List<Podcast>> = flow<List<Podcast>> {
    // Current SearchEpisodesDTO is hardcoded to EpisodeDTO.
    // For now, if searching podcasts, we might need a separate endpoint or DTO.
    // As a placeholder, we use the local search if remote podcast search isn't ready.
    // Or we can try to call search with type=podcast and handle potential issues.
    emit(emptyList())
  }.flowOn(dispatcher.io)

  override fun searchEpisodes(query: String): Flow<List<PodcastEpisode>> = flow {
    val response = api.search(mapOf("q" to query, "type" to "episode"))
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
