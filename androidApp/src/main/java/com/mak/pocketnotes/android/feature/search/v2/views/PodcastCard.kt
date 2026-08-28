package com.mak.pocketnotes.android.feature.search.v2.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast

@Composable
internal fun PodcastCard(podcast: Podcast) {
  Column(modifier = Modifier.width(140.dp)) {
    AsyncImage(
      model = podcast.thumbnail,
      contentDescription = null,
      modifier = Modifier
          .size(140.dp)
          .clip(RoundedCornerShape(8.dp)),
      contentScale = ContentScale.Crop
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = podcast.title,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Bold,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
    Text(
      text = podcast.publisher,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}
