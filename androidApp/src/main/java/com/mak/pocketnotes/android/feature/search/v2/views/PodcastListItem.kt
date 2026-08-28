package com.mak.pocketnotes.android.feature.search.v2.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun PodcastListItem(podcast: Podcast) {
  ListItem(
    headlineContent = { Text(podcast.title) },
    supportingContent = { Text(podcast.publisher) },
    leadingContent = {
      AsyncImage(
        model = podcast.thumbnail,
        contentDescription = null,
        modifier = Modifier
          .size(56.dp)
          .clip(RoundedCornerShape(4.dp)),
        contentScale = ContentScale.Crop
      )
    },
    modifier = Modifier.clickable { /* TODO */ }
  )
}

@ThemePreviews
@Composable
private fun PodcastListItemPreview() {
  PodcastListItem(podcast = samplePodcasts[0])
}
