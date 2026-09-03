package com.mak.pocketnotes.android.feature.search.v2.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun ForYouPodcastsList(
  podcasts: List<Podcast>,
  onPodcastClick: (String) -> Unit
) {
  LazyRow(
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    items(podcasts) { podcast ->
      PodcastCard(
        podcast = podcast,
        modifier = Modifier
          .width(140.dp)
          .clickable {
            onPodcastClick(podcast.id)
          }
      )
    }
  }
}

@ThemePreviews
@Composable
private fun ForYouPodcastsListPreview() {
  ForYouPodcastsList(podcasts = samplePodcasts, onPodcastClick = {})
}
