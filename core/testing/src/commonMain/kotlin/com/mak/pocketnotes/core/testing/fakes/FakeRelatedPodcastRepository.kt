package com.mak.pocketnotes.core.testing.fakes

import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.RelatedPodcastRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.RelatedPodcasts
import com.mak.pocketnotes.core.testing.util.TestPodcastData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRelatedPodcastRepository : RelatedPodcastRepository {
  var relatedToReturn: List<Podcast> = listOf(TestPodcastData.samplePodcast)

  override fun refresh(podcastId: String): Flow<RelatedPodcasts> {
    return flowOf(RelatedPodcasts(podcastId = podcastId, related = relatedToReturn))
  }

  override fun observe(podcastId: String): Flow<RelatedPodcasts> {
    return flowOf(RelatedPodcasts(podcastId = podcastId, related = relatedToReturn))
  }
}
