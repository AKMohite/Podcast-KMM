package com.mak.pocketnotes.android.feature.discover

import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.mak.pocketnotes.core.common.models.ErrorType
import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.utils.sample.sampleCuratedPodcasts
import com.mak.pocketnotes.utils.sample.samplePodcasts
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DiscoverScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun initialLoading_showsShimmer() {
    val state =
      DiscoverScreenState(
        bannerPodcastsSection = SectionState.Loading,
        trendingPodcastsSection = SectionState.Loading,
        curatedPodcastsSection = SectionState.Loading,
        isPullToRefreshing = false
      )

    discoverRobot(composeTestRule) {
      setContent(state)
      assertShimmerVisible()
      assertContentDoesNotExist()
    }
  }

  @Test
  fun successState_showsContent() {
    val state =
      DiscoverScreenState(
        bannerPodcastsSection = SectionState.Success(samplePodcasts),
        trendingPodcastsSection = SectionState.Success(samplePodcasts.take(3)),
        curatedPodcastsSection = SectionState.Success(sampleCuratedPodcasts),
        isPullToRefreshing = false
      )

    discoverRobot(composeTestRule) {
      setContent(state)
      assertContentVisible()
      assertShimmerDoesNotExist()
      assertPodcastVisible(samplePodcasts[0].title)
    }
  }

  @Test
  fun errorState_showsSnackbar() {
    var errorConsumedCalled = false
    val state =
      DiscoverScreenState(
        bannerPodcastsSection = SectionState.Success(samplePodcasts),
        trendingPodcastsSection = SectionState.Success(samplePodcasts.take(3)),
        curatedPodcastsSection = SectionState.Success(sampleCuratedPodcasts),
        isPullToRefreshing = false,
        errorType = ErrorType.SERVER_ERROR
      )

    discoverRobot(composeTestRule) {
      setContent(
        state = state,
        onErrorConsumed = { errorConsumedCalled = true }
      )
      assertRetryVisible()
      assertTrue(errorConsumedCalled)
    }
  }

  @Test
  fun retryButtonClick_callsRefreshPodcasts() {
    var refreshPodcastsCalled = false
    val state =
      DiscoverScreenState(
        bannerPodcastsSection = SectionState.Success(samplePodcasts),
        trendingPodcastsSection = SectionState.Success(samplePodcasts.take(3)),
        curatedPodcastsSection = SectionState.Success(sampleCuratedPodcasts),
        isPullToRefreshing = false,
        errorType = ErrorType.SERVER_ERROR
      )

    discoverRobot(composeTestRule) {
      setContent(
        state = state,
        refreshPodcasts = { refreshPodcastsCalled = true }
      )
      clickRetry()
      assertTrue(refreshPodcastsCalled)
    }
  }
}
