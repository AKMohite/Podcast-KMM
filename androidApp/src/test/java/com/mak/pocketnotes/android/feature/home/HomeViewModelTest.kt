package com.mak.pocketnotes.android.feature.home

import app.cash.turbine.test
import com.mak.pocketnotes.domain.models.DomainResult
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.store.BestPodcastsStore
import com.mak.pocketnotes.domain.store.CuratedPodcastsStore
import com.mak.pocketnotes.domain.usecase.RefreshBestPodcasts
import com.mak.pocketnotes.domain.usecase.RefreshCuratedPodcasts
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val refreshBestPodcasts = mockk<RefreshBestPodcasts>()
    private val refreshCuratedPodcasts = mockk<RefreshCuratedPodcasts>()
    private val getBestPodcasts = mockk<BestPodcastsStore>()
    private val getCuratedPodcasts = mockk<CuratedPodcastsStore>()

    private val testDispatcher = StandardTestDispatcher()

    private val bestPodcastsFlow = MutableStateFlow<List<Podcast>>(emptyList())
    private val curatedPodcastsFlow = MutableStateFlow<List<com.mak.pocketnotes.domain.models.CuratedPodcast>>(emptyList())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getBestPodcasts() } returns bestPodcastsFlow
        every { getCuratedPodcasts() } returns curatedPodcastsFlow
        coEvery { refreshBestPodcasts(any(), any()) } returns DomainResult.Success(emptyList())
        coEvery { refreshCuratedPodcasts(any()) } returns DomainResult.Success(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should refresh discover and observe podcasts`() = runTest {
        val viewModel = HomeViewModel(
            refreshBestPodcasts,
            refreshCuratedPodcasts,
            getBestPodcasts,
            getCuratedPodcasts
        )

        viewModel.uiState.test {
            // Initial state
            var state = awaitItem()
            assertFalse(state.refreshing)

            // State after refreshDiscover launches and updates refreshing to true
            state = awaitItem()
            assertTrue(state.refreshing)

            advanceUntilIdle()
            
            // State after refreshDiscover finishes
            state = awaitItem()
            assertFalse(state.refreshing)
        }
    }

    @Test
    fun `when podcasts are observed, uiState is updated`() = runTest {
        val podcasts = List(10) { mockk<Podcast>(relaxed = true) }
        val viewModel = HomeViewModel(
            refreshBestPodcasts,
            refreshCuratedPodcasts,
            getBestPodcasts,
            getCuratedPodcasts
        )

        viewModel.uiState.test {
            awaitItem() // refreshing=false (initial)
            awaitItem() // refreshing=true
            awaitItem() // refreshing=false
            
            bestPodcastsFlow.value = podcasts
            
            val state = awaitItem()
            // In HomeViewModel: val (topPodcasts, podcasts) = bestPodcasts.take(8).shuffled() to bestPodcasts.drop(4)
            // So state.podcasts should have 6 items (10 - 4)
            assertEquals(6, state.podcasts.size)
            assertEquals(8, state.topPodcasts.size)
            assertFalse(state.loading)
        }
    }

    @Test
    fun `refreshDiscover updates errorMsg on failure`() = runTest {
        coEvery { refreshBestPodcasts(any(), any()) } returns DomainResult.Error("Network Error")

        val viewModel = HomeViewModel(
            refreshBestPodcasts,
            refreshCuratedPodcasts,
            getBestPodcasts,
            getCuratedPodcasts
        )

        viewModel.uiState.test {
            awaitItem() // refreshing=false
            awaitItem() // refreshing=true
            
            advanceUntilIdle()

            val errorState = awaitItem()
            assertEquals("Network Error", errorState.errorMsg)
            assertFalse(errorState.refreshing)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
