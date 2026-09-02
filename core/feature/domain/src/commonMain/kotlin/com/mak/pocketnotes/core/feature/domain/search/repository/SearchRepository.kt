package com.mak.pocketnotes.core.feature.domain.search.repository

import androidx.paging.PagingData
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
  fun searchPodcasts(query: String): Flow<PagingData<Podcast>>
  fun searchPodcastsList(query: String): Flow<List<Podcast>>
  fun searchEpisodes(query: String): Flow<List<PodcastEpisode>>
  fun getLocalSuggestions(query: String): Flow<List<Podcast>>
}
