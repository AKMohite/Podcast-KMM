package com.mak.pocketnotes.android.feature.search.v2.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.search.v2.SearchUiState

@Composable
internal fun SearchIdleView(
  uiState: SearchUiState,
  padding: PaddingValues,
  isMedium: Boolean = false
) {
  LazyColumn(
    contentPadding = padding,
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.fillMaxSize()
  ) {
    item {
      SectionHeader(stringResource(R.string.for_you_header), onSeeAll = {})
      ForYouPodcastsList(uiState.forYouPodcasts)
    }

    if (!isMedium) {
      item {
        SectionHeader(stringResource(R.string.recent_searches_header), onSeeAll = null)
        RecentSearchesHorizontal(uiState.recentSearches)
      }
    }

    item {
      SectionHeader(stringResource(R.string.browse_genre_header), onSeeAll = null)
      GenresGrid(uiState.genres)
    }

    if (!isMedium) {
      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
