package com.mak.pocketnotes.android.feature.discover

import app.cash.turbine.test
import com.mak.pocketnotes.core.common.models.ErrorType
import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.core.feature.domain.home.usecase.GetDiscoverFeedUseCase
import com.mak.pocketnotes.core.testing.fakes.FakeBestPodcastRepository
import com.mak.pocketnotes.core.testing.fakes.FakeCuratedPodcastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DiscoverViewModelTest {
  private val bestPodcastRepository = FakeBestPodcastRepository()
  private val curatedPodcastRepository = FakeCuratedPodcastRepository()
  private val getDiscoverFeedUseCase =
    GetDiscoverFeedUseCase(bestPodcastRepository, curatedPodcastRepository)
  private val testDispatcher = UnconfinedTestDispatcher()

  private lateinit var viewModel: DiscoverViewmodel

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `initial state is success when repositories return data`() = runTest {
    viewModel = DiscoverViewmodel(getDiscoverFeedUseCase)

    viewModel.uiState.test {
      val state = awaitItem()
      assertTrue(state.bannerPodcastsSection is SectionState.Success)
      assertTrue(state.trendingPodcastsSection is SectionState.Success)
      assertTrue(state.curatedPodcastsSection is SectionState.Success)
      assertFalse(state.isPullToRefreshing)
    }
  }

  @Test
  fun `error in section updates errorType in state`() = runTest {
    bestPodcastRepository.sectionState = SectionState.Error(ErrorType.SERVER_ERROR)

    viewModel = DiscoverViewmodel(getDiscoverFeedUseCase)

    viewModel.uiState.test {
      val state = awaitItem()
      assertEquals(ErrorType.SERVER_ERROR, state.errorType)
    }
  }

  @Test
  fun `refreshPodcasts triggers forced refresh in repositories`() = runTest {
    viewModel = DiscoverViewmodel(getDiscoverFeedUseCase)

    viewModel.uiState.test {
      awaitItem()

      bestPodcastRepository.refreshCalls.first { !it.forceRefresh }
      curatedPodcastRepository.refreshCalls.first { !it.forceRefresh }

      viewModel.refreshPodcasts()

      assertTrue(bestPodcastRepository.refreshCalls.first { it.forceRefresh }.forceRefresh)
      assertTrue(curatedPodcastRepository.refreshCalls.first { it.forceRefresh }.forceRefresh)

      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `onErrorConsumed clears errorType`() = runTest {
    bestPodcastRepository.sectionState = SectionState.Error(ErrorType.SERVER_ERROR)
    viewModel = DiscoverViewmodel(getDiscoverFeedUseCase)

    viewModel.uiState.test {
      assertEquals(ErrorType.SERVER_ERROR, awaitItem().errorType)

      viewModel.onErrorConsumed()

      assertEquals(null, awaitItem().errorType)
    }
  }
}
