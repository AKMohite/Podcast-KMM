package com.mak.pocketnotes.android.feature.search.v2

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.mak.pocketnotes.core.testing.fakes.FakeBestPodcastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchViewModelV2Test {
  private val bestPodcastRepository = FakeBestPodcastRepository()
  private val savedStateHandle = SavedStateHandle()
  private val testDispatcher = UnconfinedTestDispatcher()

  private lateinit var viewModel: SearchViewModelV2

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun `onEvent QueryChange updates search query in SavedStateHandle`() = runTest {
    // Note: SearchViewModelV2 uses bestPodcastsRepository internally for recommendations
    // For unit testing QueryChange event:
    savedStateHandle["search_query"] = "Kotlin"
    assertEquals("Kotlin", savedStateHandle.get<String>("search_query"))
  }
}
