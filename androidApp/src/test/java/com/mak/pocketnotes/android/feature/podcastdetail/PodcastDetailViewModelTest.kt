package com.mak.pocketnotes.android.feature.podcastdetail

import app.cash.turbine.test
import com.mak.pocketnotes.core.feature.domain.podcastdetails.usecase.GetPodcastDetailsUseCase
import com.mak.pocketnotes.core.feature.domain.podcastdetails.usecase.TogglePodcastSubscriptionUseCase
import com.mak.pocketnotes.core.testing.fakes.FakeEpisodeRepository
import com.mak.pocketnotes.core.testing.fakes.FakePodcastRepository
import com.mak.pocketnotes.core.testing.fakes.FakeRelatedPodcastRepository
import kotlinx.coroutines.Dispatchers
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

class PodcastDetailViewModelTest {
  private val podcastRepository = FakePodcastRepository()
  private val relatedPodcastRepository = FakeRelatedPodcastRepository()
  private val episodeRepository = FakeEpisodeRepository()

  private val getPodcastDetailsUseCase = GetPodcastDetailsUseCase(podcastRepository, relatedPodcastRepository)
  private val togglePodcastSubscriptionUseCase = TogglePodcastSubscriptionUseCase(podcastRepository)

  private val testDispatcher = UnconfinedTestDispatcher()
  private lateinit var viewModel: PodcastDetailViewModel

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `init loads podcast details and sets uiState`() = runTest {
    viewModel = PodcastDetailViewModel(
      getPodcastDetailsUseCase = getPodcastDetailsUseCase,
      togglePodcastSubscriptionUseCase = togglePodcastSubscriptionUseCase,
      episodeRepository = episodeRepository,
      podcastId = "p1"
    )

    viewModel.uiState.test {
      val state = awaitItem()
      assertFalse(state.loading)
      assertEquals("p1", state.podcast?.id)
      assertFalse(state.isSubscribed)
    }
  }

  @Test
  fun `toggleSubscription updates subscription state`() = runTest {
    viewModel = PodcastDetailViewModel(
      getPodcastDetailsUseCase = getPodcastDetailsUseCase,
      togglePodcastSubscriptionUseCase = togglePodcastSubscriptionUseCase,
      episodeRepository = episodeRepository,
      podcastId = "p1"
    )

    viewModel.uiState.test {
      val initial = awaitItem()
      assertFalse(initial.isSubscribed)

      viewModel.toggleSubscription()

      val updated = awaitItem()
      assertTrue(updated.isSubscribed)
    }
  }
}
