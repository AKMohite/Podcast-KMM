package com.mak.pocketnotes.android.feature.search.v2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExpandedDockedSearchBarWithGap
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarWithGapState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.search.v2.views.SearchIdleView
import com.mak.pocketnotes.android.feature.search.v2.views.SearchOverlayContent
import com.mak.pocketnotes.android.feature.search.v2.views.SearchResultsView
import com.mak.pocketnotes.android.feature.search.v2.views.TrendingSearchesSidebar
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.search.models.Genre
import com.mak.pocketnotes.utils.sample.samplePodcasts
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
internal fun MediumSearchScreen(
  uiState: SearchUiState,
  searchQuery: String,
  screenState: SearchScreenState,
  searchResults: LazyPagingItems<Podcast>,
  onEvent: (SearchUiEvent) -> Unit
) {
  val textFieldState = rememberTextFieldState(searchQuery)
  val searchBarState = rememberSearchBarWithGapState()
  val scope = rememberCoroutineScope()
  val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
  val appBarWithSearchColors = SearchBarDefaults.appBarWithSearchColors()

  LaunchedEffect(searchBarState.currentValue) {
    onEvent(SearchUiEvent.SearchFocusChange(searchBarState.currentValue == SearchBarValue.Expanded))
  }

  LaunchedEffect(textFieldState.text) {
    snapshotFlow { textFieldState.text.toString() }
      .collectLatest { onEvent(SearchUiEvent.QueryChange(it)) }
  }

  val inputField =
    @Composable {
      SearchBarDefaults.InputField(
        textFieldState = textFieldState,
        searchBarState = searchBarState,
        colors = appBarWithSearchColors.searchBarColors.inputFieldColors,
        onSearch = {
          onEvent(SearchUiEvent.SearchSubmit(it))
          scope.launch { searchBarState.animateToCollapsed() }
        },
        placeholder = {
          Text(
            modifier = Modifier.clearAndSetSemantics {},
            text = stringResource(R.string.search_query_placeholder)
          )
        },
        leadingIcon = {
          SearchbarLeadingIcon(
            searchBarState = searchBarState,
            scope = scope,
            onBack = { onEvent(SearchUiEvent.SearchBack) }
          )
        },
        trailingIcon = { SearchbarTrailingIcon() }
      )
    }

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      AppBarWithSearch(
        scrollBehavior = scrollBehavior,
        state = searchBarState,
        colors = appBarWithSearchColors,
        inputField = inputField
      )
      ExpandedDockedSearchBarWithGap(state = searchBarState, inputField = inputField) {
        SearchOverlayContent(
          uiState = uiState,
          onSuggestionClick = { result ->
            textFieldState.setTextAndPlaceCursorAtEnd(result)
            onEvent(SearchUiEvent.SearchSubmit(result))
            scope.launch { searchBarState.animateToCollapsed() }
          },
          onRecentDelete = { onEvent(SearchUiEvent.RecentSearchDelete(it)) },
          onClearRecent = { onEvent(SearchUiEvent.ClearRecentSearches) }
        )
      }
    }
  ) { padding ->
    Row(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      Column(modifier = Modifier.weight(1f)) {
        if (screenState == SearchScreenState.RESULTS) {
          SearchResultsView(
            podcasts = searchResults,
            padding = PaddingValues(0.dp),
            onPodcastClick = { id -> onEvent(SearchUiEvent.OnPodcastClick(id)) }
          )
        } else {
          SearchIdleView(
            uiState = uiState,
            padding = PaddingValues(0.dp),
            isMedium = true,
            onPodcastClick = { id ->
              onEvent(SearchUiEvent.OnPodcastClick(id))
            }
          )
        }
      }
      AnimatedVisibility(screenState != SearchScreenState.RESULTS && uiState.trendingSearches.isNotEmpty()) {
        TrendingSearchesSidebar(uiState.trendingSearches)
      }
    }
  }
}

@ThemePreviews
@Composable
private fun MediumSearchScreenIdlePreview() {
  PocketNotesTheme {
    MediumSearchScreen(
      uiState = SearchUiState(
        forYouPodcasts = samplePodcasts.take(5),
        genres = listOf(Genre(1, "True Crime", 0), Genre(2, "Comedy", 0)),
        trendingSearches = listOf("Design Matters", "The Daily")
      ),
      searchQuery = "",
      screenState = SearchScreenState.IDLE,
      onEvent = {},
      searchResults = flowOf(PagingData.from(samplePodcasts.take(5))).collectAsLazyPagingItems()
    )
  }
}

@ThemePreviews
@Composable
private fun MediumSearchScreenResultsPreview() {
  MediumSearchScreen(
    uiState = SearchUiState(),
    searchQuery = "The Daily",
    screenState = SearchScreenState.RESULTS,
    onEvent = {},
    searchResults = flowOf(PagingData.from(samplePodcasts.take(5))).collectAsLazyPagingItems()
  )
}
