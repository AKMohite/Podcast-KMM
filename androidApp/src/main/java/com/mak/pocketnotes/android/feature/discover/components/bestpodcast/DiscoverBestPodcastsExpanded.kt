package com.mak.pocketnotes.android.feature.discover.components.bestpodcast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun DiscoverBestPodcastsExpanded(
    modifier: Modifier = Modifier,
    gotoDetails: (String) -> Unit,
    podcasts: List<Podcast>,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.trending),
            style = MaterialTheme.typography.titleLarge
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            val spacing = 12.dp
            val itemWidth = (maxWidth - (spacing * 4)) / 5

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 5
            ) {
                podcasts.forEach { podcast ->
                    PodcastColumn(
                        modifier = Modifier
                            .width(itemWidth)
                            .clickable { gotoDetails(podcast.id) },
                        title = podcast.title,
                        publisher = podcast.publisher,
                        image = podcast.thumbnail
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 900)
@Composable
private fun DiscoverBestPodcastsExpandedPreview() {
    PocketNotesTheme {
        Surface {
            DiscoverBestPodcastsExpanded(
                podcasts = samplePodcasts.take(8),
                gotoDetails = {}
            )
        }
    }
}
