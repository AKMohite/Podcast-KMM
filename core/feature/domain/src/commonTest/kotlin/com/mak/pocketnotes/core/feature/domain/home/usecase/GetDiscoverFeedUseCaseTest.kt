package com.mak.pocketnotes.core.feature.domain.home.usecase

import app.cash.turbine.test
import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.core.testing.fakes.FakeBestPodcastRepository
import com.mak.pocketnotes.core.testing.fakes.FakeCuratedPodcastRepository
import com.mak.pocketnotes.core.testing.util.TestPodcastData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class GetDiscoverFeedUseCaseTest {
  private val bestPodcastRepository = FakeBestPodcastRepository()
  private val curatedPodcastRepository = FakeCuratedPodcastRepository()

  private val useCase = GetDiscoverFeedUseCase(bestPodcastRepository, curatedPodcastRepository)

  @Test
  fun invoke_returnsCombinedFeedState() = runTest {
    useCase(forceRefresh = false).test {
      val feed = awaitItem()
      assertTrue(feed.bannerSection is SectionState.Success)
      assertTrue(feed.trendingSection is SectionState.Success)
      assertTrue(feed.curatedSection is SectionState.Success)
      assertEquals(
        TestPodcastData.samplePodcastList,
        (feed.bannerSection as SectionState.Success).data
      )
      assertFalse(feed.isPullToRefreshing)
    }
  }

  @Test
  fun invoke_whenForceRefreshIsTrue_triggersRefreshWithForceTrue() = runTest {
    useCase(forceRefresh = true).test {
      awaitItem()
      assertTrue(bestPodcastRepository.refreshCalls.any { it.forceRefresh })
      assertTrue(curatedPodcastRepository.refreshCalls.any { it.forceRefresh })
    }
  }
}
