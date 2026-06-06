package com.mak.pocketnotes.android.feature.player.v2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.domain.models.PodcastEpisode

@Composable
internal fun EpisodeInfoPanel(
    episode: PodcastEpisode?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(16.dp),
            )
            .padding(16.dp),
    ) {
        Text(
            text = "About this episode",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = episode?.description ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            overflow = TextOverflow.Ellipsis,
        )

        // Chapters list (if available)
        /*if ((episode?.chapters?.size ?: 0) > 0) {
            Spacer(Modifier.height(16.dp))
            Text(text = "Chapters", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            LazyColumn {
                itemsIndexed(episode!!.chapters) { _, chapter ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = formatDuration(chapter.startMs),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.width(52.dp),
                        )
                        Text(
                            text = chapter.title,
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                }
            }
        }*/
    }
}