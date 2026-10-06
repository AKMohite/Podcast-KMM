package com.mak.pocketnotes.core.testing.fakes

import androidx.paging.PagingData
import com.mak.pocketnotes.core.feature.domain.home.models.EpisodeQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.EpisodeRepository
import com.mak.pocketnotes.core.testing.util.TestPodcastData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeEpisodeRepository : EpisodeRepository {
  var episodesToReturn: List<PodcastEpisode> = listOf(TestPodcastData.sampleEpisode)

  override fun refresh(params: EpisodeQueryParam): Flow<List<PodcastEpisode>> {
    return flowOf(episodesToReturn)
  }

  override fun observeEpisodes(params: EpisodeQueryParam): Flow<List<PodcastEpisode>> {
    return flowOf(episodesToReturn)
  }

  override fun getEpisodesPaging(podcastId: String): Flow<PagingData<PodcastEpisode>> {
    return flowOf(PagingData.from(episodesToReturn))
  }

  override fun getEpisodesPagingV2(podcastId: String): Flow<PagingData<PodcastEpisode>> {
    return flowOf(PagingData.from(episodesToReturn))
  }

  override suspend fun getEpisodeById(id: String): PodcastEpisode? {
    return episodesToReturn.find { it.id == id }
  }
}
