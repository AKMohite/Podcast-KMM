package com.mak.pocketnotes.android.feature.search.v2

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExpandedDockedSearchBarWithGap
import androidx.compose.material3.ExpandedFullScreenContainedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults.rememberTooltipPositionProvider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.rememberContainedSearchBarState
import androidx.compose.material3.rememberSearchBarWithGapState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.navigation.Navigator
import com.mak.pocketnotes.android.ui.theme.isMedium
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


fun EntryProviderScope<NavKey>.searchEntryV2(navigator: Navigator) {
  entry<Search> {
    SearchScreenV2()
  }
}

@Composable
internal fun SearchScreenV2() {
  val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
  when {
    sizeClass.isMedium() -> MediumSearchScreen()
    else -> {
      CompactSearchScreen()
    }

  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompactSearchScreen() {
  val viewModel: SearchViewModelV2 = koinViewModel()
  val searchResults by viewModel.searchResultsState.collectAsStateWithLifecycle()
  val textFieldState = rememberTextFieldState()
  val searchBarState = rememberContainedSearchBarState()
  val scope = rememberCoroutineScope()
  val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
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
          scope.launch { searchBarState.animateToCollapsed() }
        },
        placeholder = {
          Text(modifier = Modifier.clearAndSetSemantics {}, text = "Search")
        },
        leadingIcon = { SearchbarLeadingIcon(searchBarState, scope) },
        trailingIcon = { SearchbarTrailingIcon() },
      )
    }

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      AppBarWithSearch(
        scrollBehavior = scrollBehavior,
        state = searchBarState,
        colors = appBarWithSearchColors,
        inputField = inputField,
//        navigationIcon = { SampleNavigationIcon(searchBarState, isAnimated = true) },
//        actions = { SampleActions(searchBarState, isAnimated = true) },
      )
//      SearchBar(state = searchBarState, inputField = inputField)
      ExpandedFullScreenContainedSearchBar(
        state = searchBarState,
        inputField = inputField,
        colors = appBarWithSearchColors.searchBarColors,
      ) {
        SampleSearchQuerySuggestions(
          onResultClick = { result ->
            textFieldState.setTextAndPlaceCursorAtEnd(result)
            scope.launch { searchBarState.animateToCollapsed() }
          }
        )
      }
    },
  ) { padding ->
    LazyColumn(contentPadding = padding, verticalArrangement = Arrangement.spacedBy(8.dp)) {
      val list = List(100) { "Text $it" }
      items(count = list.size) {
        Text(
          text = list[it],
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediumSearchScreen() {
  val textFieldState = rememberTextFieldState()
  val searchBarState = rememberSearchBarWithGapState()
  val scope = rememberCoroutineScope()
  val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
  val appBarWithSearchColors = SearchBarDefaults.appBarWithSearchColors()
  val inputField =
    @Composable {
      SearchBarDefaults.InputField(
        textFieldState = textFieldState,
        searchBarState = searchBarState,
        colors = appBarWithSearchColors.searchBarColors.inputFieldColors,
        onSearch = {
          scope.launch { searchBarState.animateToCollapsed() }
        },
        placeholder = {
          Text(modifier = Modifier.clearAndSetSemantics {}, text = "Search")
        },
        leadingIcon = { SearchbarLeadingIcon(searchBarState, scope) },
        trailingIcon = { SearchbarTrailingIcon() },
      )
    }

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      AppBarWithSearch(
        scrollBehavior = scrollBehavior,
        state = searchBarState,
        colors = appBarWithSearchColors,
        inputField = inputField,
//        navigationIcon = { SampleNavigationIcon(searchBarState) },
//        actions = { SampleActions(searchBarState) },
      )
      ExpandedDockedSearchBarWithGap(state = searchBarState, inputField = inputField) {
        SampleSearchQuerySuggestions(
          onResultClick = { result ->
            textFieldState.setTextAndPlaceCursorAtEnd(result)
            scope.launch { searchBarState.animateToCollapsed() }
          }
        )
      }
    },
  ) { padding ->
    LazyColumn(contentPadding = padding, verticalArrangement = Arrangement.spacedBy(8.dp)) {
      val list = List(100) { "Text $it" }
      items(count = list.size) {
        Text(
          text = list[it],
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        )
      }
    }
  }
}


@Composable
private fun SampleSearchQuerySuggestions(
  onResultClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier.verticalScroll(rememberScrollState())) {
    repeat(10) { idx ->
      val resultText = "Suggestion $idx"
      ListItem(
        content = { Text(resultText) },
        supportingContent = { Text("Additional info") },
        leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier =
          Modifier
            .clickable { onResultClick(resultText) }
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
      )
    }
  }
}

@Composable
private fun SearchbarLeadingIcon(searchBarState: SearchBarState, scope: CoroutineScope) =
  if (searchBarState.currentValue == SearchBarValue.Expanded) {
    TooltipBox(
      positionProvider =
        rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
      tooltip = {
        PlainTooltip(
          modifier =
            Modifier.semantics {
              // TODO(b/496338253): Remove this modifier once bug where tooltip text
              //  is not announced by a11y screen readers is resolved.
              liveRegion = LiveRegionMode.Assertive
              paneTitle = "Back"
            }
        ) {
          Text("Back")
        }
      },
      state = rememberTooltipState(),
    ) {
      IconButton(onClick = { scope.launch { searchBarState.animateToCollapsed() } }) {
        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
      }
    }
  } else {
    Icon(Icons.Default.Search, contentDescription = null)
  }

@Composable
private fun SearchbarTrailingIcon() =
  TooltipBox(
    positionProvider =
      rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
    tooltip = {
      PlainTooltip(
        modifier =
          Modifier.semantics {
            // TODO(b/496338253): Remove this modifier once bug where tooltip text is
            //  not announced by a11y screen readers is resolved.
            liveRegion = LiveRegionMode.Assertive
            paneTitle = "Mic"
          }
      ) {
        Text("Mic")
      }
    },
    state = rememberTooltipState(),
  ) {
    IconButton(onClick = { /* doSomething() */ }) {
      Icon(imageVector = Icons.Default.Mic, contentDescription = "Mic")
    }
  }
