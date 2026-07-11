package com.mak.pocketnotes.android.feature.discover.components.bestpodcast

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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.discover.components.PodcastRow
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isMedium
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun DiscoverBestPodcastsCompact(
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

@ThemePreviews
@Composable
private fun DiscoverBestPodcastsCompactPreview() {
    DiscoverBestPodcastsCompact(
        podcasts = samplePodcasts.take(8),
        gotoDetails = {}
    )
}
