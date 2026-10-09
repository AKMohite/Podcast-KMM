package com.mak.pocketnotes.core.feature.domain.podcastdetails.usecase

import app.cash.turbine.test
import com.mak.pocketnotes.core.testing.fakes.FakePodcastRepository
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class TogglePodcastSubscriptionUseCaseTest {
  private val podcastRepository = FakePodcastRepository()
  private val useCase = TogglePodcastSubscriptionUseCase(podcastRepository)

  @Test
  fun invoke_whenNotSubscribed_subscribesPodcast() = runTest {
    val podcastId = "podcast-1"
    podcastRepository.isSubscribed(podcastId).test {
      assertFalse(awaitItem())

      useCase(podcastId)

      assertTrue(awaitItem())
    }
  }

  @Test
  fun invoke_whenAlreadySubscribed_unsubscribesPodcast() = runTest {
    val podcastId = "podcast-1"
    podcastRepository.subscribe(podcastId)

    podcastRepository.isSubscribed(podcastId).test {
      assertTrue(awaitItem())

      useCase(podcastId)

      assertFalse(awaitItem())
    }
  }
}
