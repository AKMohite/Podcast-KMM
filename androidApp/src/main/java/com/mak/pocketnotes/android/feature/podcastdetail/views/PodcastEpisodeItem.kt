package com.mak.pocketnotes.android.feature.podcastdetail.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.utils.sample.sampleEpisodes

@Composable
internal fun PodcastEpisodeItem(
    episode: PodcastEpisode,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = modifier
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
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

@Preview
@Composable
private fun PodcastEpisodeItemPreview() {
    PocketNotesTheme {
        PodcastEpisodeItem(episode = sampleEpisodes[0])
    }
}