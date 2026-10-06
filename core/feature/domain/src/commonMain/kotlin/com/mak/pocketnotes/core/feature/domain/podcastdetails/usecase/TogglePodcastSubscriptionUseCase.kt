package com.mak.pocketnotes.core.feature.domain.podcastdetails.usecase

import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.PodcastRepository
import kotlinx.coroutines.flow.first

class TogglePodcastSubscriptionUseCase(
  private val podcastRepository: PodcastRepository
) {
  suspend operator fun invoke(podcastId: String) {
    val isCurrentlySubscribed = podcastRepository.isSubscribed(podcastId).first()
    if (isCurrentlySubscribed) {
      podcastRepository.unsubscribe(podcastId)
    } else {
      podcastRepository.subscribe(podcastId)
    }
  }
}
