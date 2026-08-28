package com.mak.pocketnotes.android.feature.search.v2

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.navigation.Navigator
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.android.ui.theme.isMedium
import org.koin.compose.viewmodel.koinViewModel


fun EntryProviderScope<NavKey>.searchEntryV2(navigator: Navigator) {
  entry<Search> {
    SearchScreenV2()
  }
}

@Composable
internal fun SearchScreenV2() {
  val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
  val viewModel: SearchViewModelV2 = koinViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val screenState by viewModel.screenState.collectAsStateWithLifecycle()

  BackHandler(enabled = screenState != SearchScreenState.IDLE) {
    viewModel.onEvent(SearchUiEvent.SearchBack)
  }

  when {
    sizeClass.isMedium() -> MediumSearchScreen(
      uiState = uiState,
      searchQuery = searchQuery,
      screenState = screenState,
      onEvent = viewModel::onEvent
    )
    else -> {
      CompactSearchScreen(
        uiState = uiState,
        searchQuery = searchQuery,
        screenState = screenState,
        onEvent = viewModel::onEvent
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchResultsFilters() {
  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    FilterChip(selected = true, onClick = {}, label = { Text(stringResource(R.string.all_chip)) })
    FilterChip(
      selected = false,
      onClick = {},
      label = { Text(stringResource(R.string.podcasts_chip)) })
    FilterChip(
      selected = false,
      onClick = {},
      label = { Text(stringResource(R.string.episodes_chip)) })
  }
}

@ThemePreviews
@Composable
private fun SearchResultsFiltersPreview() {
  SearchResultsFilters()
}
