package com.mak.pocketnotes.android.feature.search.v2.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.search.v2.SearchUiState
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.core.feature.domain.search.models.Genre
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun SearchIdleView(
  uiState: SearchUiState,
  padding: PaddingValues,
  isMedium: Boolean = false,
  onPodcastClick: (String) -> Unit
) {
  if (uiState.isIdleSearchLoading) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator()
    }
  } else {
    LazyColumn(
      contentPadding = padding,
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      if (uiState.forYouPodcasts.isNotEmpty()) {
        item {
          SectionHeader(stringResource(R.string.for_you_header), onSeeAll = {})
          ForYouPodcastsList(
            podcasts = uiState.forYouPodcasts,
            onPodcastClick = onPodcastClick
          )
        }
      }

      if (!isMedium && uiState.recentSearches.isNotEmpty()) {
        item {
          SectionHeader(stringResource(R.string.recent_searches_header), onSeeAll = null)
          RecentSearchesHorizontal(uiState.recentSearches)
        }
      }

      if (uiState.genres.isNotEmpty()) {
        item {
          SectionHeader(stringResource(R.string.browse_genre_header), onSeeAll = null)
          GenresGrid(uiState.genres)
        }
      }

      if (!isMedium && uiState.hasAnySections) {
        item {
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}

@ThemePreviews
@Composable
private fun SearchIdleViewPreview() {
  SearchIdleView(
    uiState = SearchUiState(
      forYouPodcasts = samplePodcasts.take(5),
      recentSearches = listOf("Android", "Kotlin", "Compose"),
      genres = listOf(
        Genre(1, "True Crime", 0),
        Genre(2, "Comedy", 0),
        Genre(3, "Technology", 0)
      )
    ),
    padding = PaddingValues(0.dp),
    onPodcastClick = {}
  )
}
