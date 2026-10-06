package com.mak.pocketnotes.core.feature.domain.podcastdetails.usecase

import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.PodcastRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.RelatedPodcastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class PodcastDetailsResult(
  val podcast: Podcast,
  val isSubscribed: Boolean
)

class GetPodcastDetailsUseCase(
  private val podcastRepository: PodcastRepository,
  private val relatedPodcastRepository: RelatedPodcastRepository
) {
  operator fun invoke(podcastId: String): Flow<PodcastDetailsResult> {
    return combine(
      podcastRepository.refresh(podcastId),
      relatedPodcastRepository.refresh(podcastId),
      podcastRepository.isSubscribed(podcastId)
    ) { podcast, recommendations, isSubscribed ->
      PodcastDetailsResult(
        podcast = podcast.copy(recommendations = recommendations.related),
        isSubscribed = isSubscribed
      )
    }
  }
}
