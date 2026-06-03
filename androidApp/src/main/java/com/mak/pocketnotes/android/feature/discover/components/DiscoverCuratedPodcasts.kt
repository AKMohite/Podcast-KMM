package com.mak.pocketnotes.android.feature.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isMedium
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.SectionPodcast
import com.mak.pocketnotes.utils.sample.sampleCuratedPodcasts

@Composable
internal fun DiscoverCuratedPodcasts(
    modifier: Modifier = Modifier,
    sizeClass: WindowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass,
    podcastSection: CuratedPodcast,
    goToDetails: (String) -> Unit
) {
    when{
        sizeClass.isExpanded() -> DiscoverCuratedPodcastsCompactAndMedium(
            modifier,
            goToDetails,
            podcastSection
        )
        else -> DiscoverCuratedPodcastsCompactAndMedium(
            modifier,
            goToDetails,
            podcastSection
        )
    }
}

@Composable
fun DiscoverCuratedPodcastsCompactAndMedium(
    modifier: Modifier = Modifier,
    goToDetails: (String) -> Unit,
    podcastSection: CuratedPodcast,
    sizeClass: WindowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass,
) {
    val columnFraction = when {
        sizeClass.isMedium() -> 0.5f
        else -> 0.8f
    }

    BoxWithConstraints(
        modifier = modifier
    ) {
        val itemWidth = maxWidth * columnFraction

        Column {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = podcastSection.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = if (sizeClass.isMedium()) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium
            )
            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 160.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = podcastSection.podcasts, key = SectionPodcast::id) { podcast ->
                    CuratedPodcastItem(
                        modifier = Modifier
                            .width(itemWidth)
                            .clickable { goToDetails(podcast.id) },
                        podcast = podcast
                    )
                }
            }
        }
    }
}

@Composable
private fun CuratedPodcastItem(
    modifier: Modifier = Modifier,
    podcast: SectionPodcast,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = podcast.thumbnail,
            contentDescription = podcast.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(70.dp)
                .clip(MaterialTheme.shapes.small),
            placeholder = debugPlaceholder()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = podcast.title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = podcast.publisher,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@PreviewScreenSizes
@Composable
private fun DiscoverCuratedPodcastsPreview() {
    PocketNotesTheme {
        Surface {
            DiscoverCuratedPodcasts(
                podcastSection = sampleCuratedPodcasts[0],
                goToDetails = {}
            )
        }
    }
}
