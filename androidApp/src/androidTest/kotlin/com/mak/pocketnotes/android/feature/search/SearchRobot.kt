package com.mak.pocketnotes.android.feature.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.mak.pocketnotes.android.feature.search.v2.CompactSearchScreen
import com.mak.pocketnotes.android.feature.search.v2.SearchScreenState
import com.mak.pocketnotes.android.feature.search.v2.SearchUiState
import kotlinx.coroutines.flow.flowOf

internal fun searchRobot(rule: ComposeContentTestRule, block: SearchRobot.() -> Unit) {
  SearchRobot(rule).block()
}

internal class SearchRobot(private val rule: ComposeContentTestRule) {
  fun setContent(
    state: SearchUiState = SearchUiState(),
    query: String = "",
    screenState: SearchScreenState = SearchScreenState.IDLE
  ) = apply {
    rule.setContent {
      val lazyPagingItems = flowOf(PagingData.empty<com.mak.pocketnotes.core.feature.domain.home.models.Podcast>()).collectAsLazyPagingItems()
      CompactSearchScreen(
        uiState = state,
        searchQuery = query,
        screenState = screenState,
        searchResults = lazyPagingItems,
        onEvent = {}
      )
    }
  }

  fun assertRecentSearchesVisible(text: String) = apply {
    rule.onNodeWithText(text).assertIsDisplayed()
  }
}
