package com.mak.pocketnotes.android.feature.search.v2

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExpandedFullScreenContainedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults.rememberTooltipPositionProvider
import androidx.compose.material3.rememberContainedSearchBarState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.search.v2.views.SearchIdleView
import com.mak.pocketnotes.android.feature.search.v2.views.SearchOverlayContent
import com.mak.pocketnotes.android.feature.search.v2.views.SearchResultsView
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.search.models.Genre
import com.mak.pocketnotes.utils.sample.samplePodcasts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CompactSearchScreen(
  uiState: SearchUiState,
  searchQuery: String,
  screenState: SearchScreenState,
  searchResults: LazyPagingItems<Podcast>,
  onEvent: (SearchUiEvent) -> Unit
) {
  val textFieldState = rememberTextFieldState(searchQuery)
  val searchBarState = rememberContainedSearchBarState()
  val scope = rememberCoroutineScope()
  val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

  LaunchedEffect(searchBarState.currentValue) {
    onEvent(SearchUiEvent.SearchFocusChange(searchBarState.currentValue == SearchBarValue.Expanded))
  }

  LaunchedEffect(textFieldState.text) {
    snapshotFlow { textFieldState.text.toString() }
      .collectLatest { onEvent(SearchUiEvent.QueryChange(it)) }
  }

  val appBarWithSearchColors =
    SearchBarDefaults.appBarWithSearchColors(
      searchBarColors = SearchBarDefaults.containedColors(state = searchBarState)
    )
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
      ExpandedFullScreenContainedSearchBar(
        state = searchBarState,
        inputField = inputField,
        colors = appBarWithSearchColors.searchBarColors
      ) {
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
    if (screenState == SearchScreenState.RESULTS) {
      SearchResultsView(
        podcasts = searchResults,
        padding = padding,
        onPodcastClick = { id -> onEvent(SearchUiEvent.OnPodcastClick(id)) }
      )
    } else {
      SearchIdleView(
        uiState = uiState,
        padding = padding,
        onPodcastClick = { id ->
          onEvent(SearchUiEvent.OnPodcastClick(id))
        }
      )
    }
  }
}

@ThemePreviews
@Composable
private fun CompactSearchScreenIdlePreview() {
  PocketNotesTheme {
    CompactSearchScreen(
      uiState = SearchUiState(
        forYouPodcasts = samplePodcasts.take(5),
        genres = listOf(Genre(1, "True Crime", 0), Genre(2, "Comedy", 0))
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
private fun CompactSearchScreenResultsPreview() {
  CompactSearchScreen(
    uiState = SearchUiState(),
    searchQuery = "The Daily",
    screenState = SearchScreenState.RESULTS,
    onEvent = {},
    searchResults = flowOf(PagingData.from(samplePodcasts.take(5))).collectAsLazyPagingItems()
  )
}

@Composable
internal fun SearchbarLeadingIcon(
  searchBarState: SearchBarState,
  scope: CoroutineScope,
  onBack: () -> Unit
) = if (searchBarState.currentValue == SearchBarValue.Expanded) {
  TooltipBox(
    positionProvider =
      rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
    tooltip = {
      PlainTooltip(
        modifier =
          Modifier.semantics {
            liveRegion = LiveRegionMode.Assertive
            paneTitle = "Back"
          }
      ) {
        Text(stringResource(R.string.back))
      }
    },
    state = rememberTooltipState()
  ) {
    IconButton(
      onClick = {
        onBack()
        scope.launch { searchBarState.animateToCollapsed() }
      }
    ) {
      Icon(
        Icons.AutoMirrored.Default.ArrowBack,
        contentDescription = stringResource(R.string.back)
      )
    }
  }
} else {
  Icon(Icons.Default.Search, contentDescription = null)
}

@Composable
internal fun SearchbarTrailingIcon() = TooltipBox(
  positionProvider =
    rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
  tooltip = {
    PlainTooltip(
      modifier =
        Modifier.semantics {
          liveRegion = LiveRegionMode.Assertive
          paneTitle = "Mic"
        }
    ) {
      Text(stringResource(R.string.text_to_speech))
    }
  },
  state = rememberTooltipState()
) {
  IconButton(onClick = { /* doSomething() */ }) {
    Icon(
      imageVector = Icons.Default.Mic,
      contentDescription = stringResource(R.string.text_to_speech)
    )
  }
}
