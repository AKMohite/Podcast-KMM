package com.mak.pocketnotes.android.feature.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
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
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowSizeClass
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.home.views.PodcastRow
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
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
    val isTablet = sizeClass.isMedium()
    val columnFraction = if (isTablet) 0.35f else 0.8f
    val rowCount = if (isTablet) 4 else 2
    val maxHeight = if (isTablet) 300.dp else 150.dp
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = stringResource(R.string.trending),
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyHorizontalGrid(
            rows = GridCells.Fixed(rowCount),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxHeight),
        ) {
            items(items = podcasts, key = Podcast::id) { podcast ->
                PodcastRow(
                    modifier = Modifier
                        .fillMaxWidth(columnFraction)
                        .clickable { gotoDetails(podcast.id) }
                        .padding(horizontal = 4.dp),
                    podcast = podcast
                )
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