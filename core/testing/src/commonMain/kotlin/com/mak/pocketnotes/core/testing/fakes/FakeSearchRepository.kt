package com.mak.pocketnotes.core.testing.fakes

import androidx.paging.PagingData
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.feature.domain.search.repository.SearchRepository
import com.mak.pocketnotes.core.testing.util.TestPodcastData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSearchRepository : SearchRepository {
  var podcastsToReturn: List<Podcast> = TestPodcastData.samplePodcastList
  var episodesToReturn: List<PodcastEpisode> = listOf(TestPodcastData.sampleEpisode)

  override fun searchPodcasts(query: String): Flow<PagingData<Podcast>> {
    return flowOf(PagingData.from(podcastsToReturn))
  }

  override fun searchPodcastsList(query: String): Flow<List<Podcast>> {
    return flowOf(podcastsToReturn)
  }

  override fun searchEpisodes(query: String): Flow<List<PodcastEpisode>> {
    return flowOf(episodesToReturn)
  }

  override fun getLocalSuggestions(query: String): Flow<List<Podcast>> {
    return flowOf(podcastsToReturn)
  }
}
