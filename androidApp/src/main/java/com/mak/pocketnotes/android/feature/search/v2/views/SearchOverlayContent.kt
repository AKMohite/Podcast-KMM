package com.mak.pocketnotes.android.feature.search.v2.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.search.v2.SearchUiState
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun SearchOverlayContent(
  uiState: SearchUiState,
  onSuggestionClick: (String) -> Unit,
  onRecentDelete: (String) -> Unit,
  onClearRecent: () -> Unit
) {
  Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
    if (uiState.suggestions.isNotEmpty()) {
      Text(
        text = stringResource(R.string.suggestions_header),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
      )
      uiState.suggestions.forEach { podcast ->
        ListItem(
          headlineContent = { Text(podcast.title) },
          supportingContent = {
            Text(
              stringResource(
                R.string.podcast_search_suggestion,
                podcast.publisher
              )
            )
          },
          leadingContent = { Icon(Icons.Default.Search, contentDescription = null) },
          modifier = Modifier.clickable { onSuggestionClick(podcast.title) }
        )
      }
    } else {
      if (uiState.recentSearches.isNotEmpty()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = stringResource(R.string.recent_header),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
          )
          TextButton(onClick = onClearRecent) {
            Text(stringResource(R.string.clear))
          }
        }
        uiState.recentSearches.forEach { term ->
          ListItem(
            headlineContent = { Text(term) },
            leadingContent = { Icon(Icons.Default.History, contentDescription = null) },
            trailingContent = {
              IconButton(onClick = { onRecentDelete(term) }) {
                Icon(Icons.Default.Close, contentDescription = null)
              }
            },
            modifier = Modifier.clickable { onSuggestionClick(term) }
          )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
      }

      if (uiState.trendingSearches.isNotEmpty()) {
        Text(
          text = stringResource(R.string.trending_now),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        uiState.trendingSearches.forEach { term ->
          ListItem(
            headlineContent = { Text(term) },
            leadingContent = {
              Icon(
                Icons.Default.TrendingUp,
                contentDescription = null
              )
            },
            modifier = Modifier.clickable { onSuggestionClick(term) }
          )
        }
      }
    }
  }
}

@ThemePreviews
@Composable
private fun SearchOverlayContentSuggestionsPreview() {
  SearchOverlayContent(
    uiState = SearchUiState(
      suggestions = samplePodcasts.take(3)
    ),
    onSuggestionClick = {},
    onRecentDelete = {},
    onClearRecent = {}
  )
}

@ThemePreviews
@Composable
private fun SearchOverlayContentRecentPreview() {
  SearchOverlayContent(
    uiState = SearchUiState(
      recentSearches = listOf("Android", "Kotlin", "Compose"),
      trendingSearches = listOf("Design Matters", "The Daily")
    ),
    onSuggestionClick = {},
    onRecentDelete = {},
    onClearRecent = {}
  )
}
