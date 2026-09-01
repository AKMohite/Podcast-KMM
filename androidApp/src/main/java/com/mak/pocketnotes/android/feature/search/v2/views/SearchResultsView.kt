package com.mak.pocketnotes.android.feature.search.v2.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.podcastdetail.views.PodcastEpisodeItem
import com.mak.pocketnotes.android.feature.search.v2.SearchResults
import com.mak.pocketnotes.android.feature.search.v2.SearchResultsFilters
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.utils.sample.sampleEpisodes
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun SearchResultsView(results: SearchResults, padding: PaddingValues) {
  LazyColumn(
    contentPadding = padding,
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    item {
      SearchResultsFilters()
    }

    results.topResult?.let { podcast ->
      item {
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
      item {
        Text(
          text = stringResource(R.string.episodes),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
      }
      items(results.episodes) { episode ->
        PodcastEpisodeItem(episode = episode, showImage = true)
      }
    }

    if (results.podcasts.isNotEmpty()) {
//      item {
//        Text(
//          text = stringResource(R.string.more_podcasts),
//          style = MaterialTheme.typography.titleMedium,
//          fontWeight = FontWeight.Bold
//        )
//      }
      items(
        items = results.podcasts,
        key = { podcast -> podcast.id }
      ) { podcast ->
        PodcastListItem(podcast)
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
      otherPodcasts = samplePodcasts.drop(1)
    ),
    padding = PaddingValues(0.dp)
  )
}
