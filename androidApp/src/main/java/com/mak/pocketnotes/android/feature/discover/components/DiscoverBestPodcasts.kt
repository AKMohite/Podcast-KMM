package com.mak.pocketnotes.android.feature.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.home.views.PodcastRow
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isMedium
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun DiscoverBestPodcasts(
    modifier: Modifier = Modifier,
    gotoDetails: (String) -> Unit,
    podcasts: List<Podcast>,
    sizeClass: WindowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
) {

    if(sizeClass.isExpanded()) {
        DiscoverBestPodcastsExpanded(
            modifier,
            gotoDetails,
            podcasts
        )
    } else {
        DiscoverBestPodcastsCompactAndMedium(
            modifier,
            gotoDetails,
            podcasts
        )
    }
}

@Composable
fun DiscoverBestPodcastsExpanded(
    modifier: Modifier = Modifier,
    gotoDetails: (String) -> Unit,
    podcasts: List<Podcast>,
) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.trending),
            style = MaterialTheme.typography.titleLarge
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = podcasts, key = Podcast::id) { podcast ->

            }
        }
    }
}

@Composable
fun DiscoverBestPodcastsCompactAndMedium(
    modifier: Modifier = Modifier,
    gotoDetails: (String) -> Unit,
    podcasts: List<Podcast>,
    sizeClass: WindowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
) {
    val columnFraction = when {
        sizeClass.isExpanded() -> 0.3f
        sizeClass.isMedium() -> 0.5f
        else -> 0.8f
    }
    val rowCount = when {
        sizeClass.isExpanded() || sizeClass.isMedium() -> 4
        else -> 2
    }

    // Calculate maxHeight based on rowCount (approx 80dp per row)
    val maxHeight = when {
        sizeClass.isExpanded() || sizeClass.isMedium() -> 320.dp
        else -> 160.dp
    }

    BoxWithConstraints(
        modifier = modifier
    ) {
        val itemWidth = maxWidth * columnFraction

        Column {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = stringResource(R.string.trending),
                style = MaterialTheme.typography.titleLarge
            )
            LazyHorizontalGrid(
                rows = GridCells.Fixed(rowCount),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = maxHeight),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = podcasts, key = Podcast::id) { podcast ->
                    PodcastRow(
                        modifier = Modifier
                            .width(itemWidth)
                            .clickable { gotoDetails(podcast.id) },
                        podcast = podcast
                    )
                }
            }
        }
    }
}

@Preview
@PreviewScreenSizes
@Composable
private fun DiscoverBestPodcastsPreview() {
    PocketNotesTheme {
        Surface {
            DiscoverBestPodcasts(
                podcasts = samplePodcasts.take(8),
                gotoDetails = {}
            )
        }
    }
}
