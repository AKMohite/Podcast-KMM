package com.mak.pocketnotes.android.feature.search.v2.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.mak.pocketnotes.android.feature.search.v2.SearchResultsFilters
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isMedium
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast

@Composable
internal fun SearchResultsView(
  podcasts: LazyPagingItems<Podcast>,
  padding: PaddingValues,
  onPodcastClick: (String) -> Unit
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

    items(
      count = podcasts.itemCount,
      key = podcasts.itemKey { it.id },
      contentType = podcasts.itemContentType { "podcast" }
    ) { index ->
      val podcast = podcasts[index]
      if (podcast != null) {
        if (columns > 1) {
          PodcastCard(
            podcast = podcast,
            modifier = Modifier
              .clickable {
                onPodcastClick(podcast.id)
              }
          )
        } else {
          PodcastListItem(
            podcast = podcast,
            modifier = Modifier
              .clickable {
                onPodcastClick(podcast.id)
              }
          )
        }
      }
    }

    when (podcasts.loadState.append) {
      is LoadState.Loading -> {
        item(span = { GridItemSpan(maxLineSpan) }) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            contentAlignment = Alignment.Center
          ) {
            CircularProgressIndicator()
          }
        }
      }

      is LoadState.Error -> {
        item(span = { GridItemSpan(maxLineSpan) }) {
          val error = (podcasts.loadState.append as LoadState.Error).error
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            contentAlignment = Alignment.Center
          ) {
            androidx.compose.material3.Text(
              text = error.message ?: "An unexpected error occurred",
              color = androidx.compose.material3.MaterialTheme.colorScheme.error
            )
          }
        }
      }

      else -> Unit
    }
  }
}
