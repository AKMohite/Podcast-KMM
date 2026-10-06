package com.mak.pocketnotes.core.feature.domain.podcastdetails.usecase

import app.cash.turbine.test
import com.mak.pocketnotes.core.testing.fakes.FakePodcastRepository
import com.mak.pocketnotes.core.testing.fakes.FakeRelatedPodcastRepository
import com.mak.pocketnotes.core.testing.util.TestPodcastData
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetPodcastDetailsUseCaseTest {
  private val podcastRepository = FakePodcastRepository()
  private val relatedPodcastRepository = FakeRelatedPodcastRepository()

  private val getDetailsUseCase = GetPodcastDetailsUseCase(podcastRepository, relatedPodcastRepository)
  private val toggleSubscriptionUseCase = TogglePodcastSubscriptionUseCase(podcastRepository)

  @Test
  fun invoke_combinesPodcastDetailsRecommendationsAndSubscription() = runTest {
    getDetailsUseCase("p1").test {
      val details = awaitItem()
      assertEquals("p1", details.podcast.id)
      assertEquals(listOf(TestPodcastData.samplePodcast), details.podcast.recommendations)
      assertFalse(details.isSubscribed)
    }
  }

  @Test
  fun toggleSubscription_subscribesAndUnsubscribesSuccessfully() = runTest {
    toggleSubscriptionUseCase("p1")

    getDetailsUseCase("p1").test {
      val details = awaitItem()
      assertTrue(details.isSubscribed)
    }

    toggleSubscriptionUseCase("p1")

    getDetailsUseCase("p1").test {
      val details = awaitItem()
      assertFalse(details.isSubscribed)
    }
  }
}
