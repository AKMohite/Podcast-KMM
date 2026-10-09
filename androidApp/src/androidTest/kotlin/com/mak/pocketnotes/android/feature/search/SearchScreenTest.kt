package com.mak.pocketnotes.android.feature.search

import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.mak.pocketnotes.android.feature.search.v2.SearchUiState
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun idleSearch_showsRecentSearchesAndGenres() {
    val state = SearchUiState(
      recentSearches = listOf("Tech Podcast")
    )

    searchRobot(composeTestRule) {
      setContent(state = state)
      assertRecentSearchesVisible("Tech Podcast")
    }
  }
}
