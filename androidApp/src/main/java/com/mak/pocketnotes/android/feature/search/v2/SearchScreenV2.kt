package com.mak.pocketnotes.android.feature.search.v2

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExpandedDockedSearchBar
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.navigation.Navigator
import com.mak.pocketnotes.android.ui.theme.isMedium
import kotlinx.coroutines.launch


fun EntryProviderScope<NavKey>.searchEntryV2(navigator: Navigator) {
  entry<Search> {
    SearchScreenV2()
  }
}

@Composable
internal fun SearchScreenV2() {
  val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
  when {
    sizeClass.isMedium() -> DockedSearchField()
    else -> FullscreenSearchbarField()
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullscreenSearchbarField() {
  val searchBarState = rememberSearchBarState()
  val scope = rememberCoroutineScope()
  val textFieldState = rememberTextFieldState()

  val isExpanded = searchBarState.currentValue == SearchBarValue.Expanded

  val inputField = @Composable {
    SearchBarDefaults.InputField(
      textFieldState = textFieldState,
      searchBarState = searchBarState,
      onSearch = { query ->
        scope.launch { searchBarState.animateToCollapsed() }
      },
      placeholder = { Text("Search podcasts or episodes...") },
      leadingIcon = {
        if (isExpanded) { /* Back button */
        } else { /* Search Icon */
        }
      }
    )
  }

  SearchBar(
    state = searchBarState,
    inputField = inputField
  )

  if (isExpanded) {
    ExpandedFullScreenSearchBar(
      state = searchBarState,
      inputField = inputField
    ) {
      Text(
        text = "Search full screen content"
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DockedSearchField() {
  val searchBarState = rememberSearchBarState()
  val scope = rememberCoroutineScope()
  val textFieldState = rememberTextFieldState()

  val isExpanded = searchBarState.currentValue == SearchBarValue.Expanded

  val inputField = @Composable {
    SearchBarDefaults.InputField(
      textFieldState = textFieldState,
      searchBarState = searchBarState,
      onSearch = { query ->
        scope.launch { searchBarState.animateToCollapsed() }
      },
      placeholder = { Text("Search podcasts or episodes...") }
    )
  }

  SearchBar(
    state = searchBarState,
    inputField = inputField
  )

  if (isExpanded) {
    ExpandedDockedSearchBar(
      state = searchBarState,
      inputField = inputField
    ) {
      Text(
        text = "Search docked content"
      )
    }
  }
}
