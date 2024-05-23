package com.mak.pocketnotes.android.feature.podcastdetail.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.utils.sample.sampleEpisodes

@Composable
internal fun PodcastEpisodeItem(
    episode: PodcastEpisode,
    modifier: Modifier = Modifier,
    showImage: Boolean = false
) {

    Card(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showImage) {
                AsyncImage(
                    model = episode.thumbnail,
                    contentDescription = episode.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    placeholder = debugPlaceholder()
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = episode.readableTime(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Thin
                )
                Text(
                    text = episode.title,
                    maxLines = 2,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = episode.readableDuration(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

}

@Preview
@Composable
private fun PodcastEpisodeItemPreview() {
    PocketNotesTheme {
        PodcastEpisodeItem(
            episode = sampleEpisodes[0],
            showImage = true
        )
    }
}