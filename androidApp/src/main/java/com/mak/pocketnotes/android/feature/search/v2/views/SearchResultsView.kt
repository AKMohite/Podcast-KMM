package com.mak.pocketnotes.android.feature.search.v2.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.podcastdetail.views.PodcastEpisodeItem
import com.mak.pocketnotes.android.feature.search.v2.SearchResults
import com.mak.pocketnotes.android.feature.search.v2.SearchResultsFilters
import com.mak.pocketnotes.android.ui.theme.ExpandedPreviews
import com.mak.pocketnotes.android.ui.theme.MediumPreviews
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isMedium
import com.mak.pocketnotes.utils.sample.sampleEpisodes
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun SearchResultsView(
  results: SearchResults,
  padding: PaddingValues
) {
  val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
  val columns = when {
    sizeClass.isExpanded() -> 4
    sizeClass.isMedium() -> 2
    else -> 1
  }

  LazyVerticalGrid(
    columns = GridCells.Fixed(columns),
    contentPadding = padding,
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    item(span = { GridItemSpan(maxLineSpan) }) {
      SearchResultsFilters()
    }

    results.topResult?.let { podcast ->
      item(span = { GridItemSpan(maxLineSpan) }) {
        Text(
          text = stringResource(R.string.top_result),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        TopResultCard(podcast)
      }
    }

    if (results.episodes.isNotEmpty()) {
      item(span = { GridItemSpan(maxLineSpan) }) {
        Text(
          text = stringResource(R.string.episodes),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }
      items(
        items = results.episodes,
        span = { GridItemSpan(maxLineSpan) }
      ) { episode ->
        PodcastEpisodeItem(episode = episode, showImage = true)
      }
    }

    if (results.podcasts.isNotEmpty()) {
      items(
        items = results.podcasts,
        key = { podcast -> podcast.id }
      ) { podcast ->
        if (columns > 1) {
          PodcastCard(podcast)
        } else {
          PodcastListItem(podcast)
        }
      }
    }
  }
}

@ThemePreviews
@Composable
private fun SearchResultsViewPreview() {
  SearchResultsView(
    results = SearchResults(
      topResult = samplePodcasts[0],
      episodes = sampleEpisodes,
      podcasts = samplePodcasts.drop(1)
    ),
    padding = PaddingValues(0.dp)
  )
}

@MediumPreviews
@Composable
private fun SearchResultsViewMediumPreview() {
  SearchResultsView(
    results = SearchResults(
      topResult = samplePodcasts[0],
      episodes = sampleEpisodes,
      podcasts = samplePodcasts.drop(1)
    ),
    padding = PaddingValues(0.dp)
  )
}

@ExpandedPreviews
@Composable
private fun SearchResultsViewExpandedPreview() {
  SearchResultsView(
    results = SearchResults(
      topResult = samplePodcasts[0],
      episodes = sampleEpisodes,
      podcasts = samplePodcasts.drop(1)
    ),
    padding = PaddingValues(0.dp)
  )
}
