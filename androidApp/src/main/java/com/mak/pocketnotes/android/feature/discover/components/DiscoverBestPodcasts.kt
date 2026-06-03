package com.mak.pocketnotes.android.feature.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
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

    when{
        sizeClass.isExpanded() -> DiscoverBestPodcastsExpanded(
            modifier,
            gotoDetails,
            podcasts.take(10)
        )
        sizeClass.isMedium() -> DiscoverBestPodcastsMedium(
            modifier,
            gotoDetails,
            podcasts
        )
        else -> DiscoverBestPodcastsCompact(
            modifier,
            gotoDetails,
            podcasts
        )
    }
}

@Composable
fun DiscoverBestPodcastsMedium(
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

        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(bottom = 16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = podcasts.size,
                span = { index ->
                    if (index % 3 == 0) GridItemSpan(2) else GridItemSpan(1)
                }
            ) { index ->
                val podcast = podcasts[index]
                if (index % 3 == 0) {
                    FeaturedPodcastCard(
                        modifier = Modifier.width(440.dp),
                        podcast = podcast,
                        gotoDetails = gotoDetails,
                        isPopular = index == 0
                    )
                } else {
                    SmallPodcastCard(
                        modifier = Modifier.width(260.dp),
                        podcast = podcast,
                        gotoDetails = gotoDetails
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedPodcastCard(
    podcast: Podcast,
    gotoDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPopular: Boolean = false
) {
    Surface(
        modifier = modifier.clickable { gotoDetails(podcast.id) },
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isPopular) {
                    Surface(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = CircleShape
                    ) {
                        Text(
                            text = stringResource(R.string.most_popular),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }

                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = podcast.publisher,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = podcast.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            AsyncImage(
                model = podcast.thumbnail,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 8.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
                placeholder = debugPlaceholder()
            )
        }
    }
}

@Composable
private fun SmallPodcastCard(
    podcast: Podcast,
    gotoDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { gotoDetails(podcast.id) },
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = podcast.publisher,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            AsyncImage(
                model = podcast.thumbnail,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop,
                placeholder = debugPlaceholder()
            )
        }
    }
}

@Composable
fun DiscoverBestPodcastsExpanded(
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
                        podcast = podcast
                    )
                }
            }
        }
    }
}

@Composable
private fun PodcastColumn(
    podcast: Podcast,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            model = podcast.thumbnail,
            contentDescription = podcast.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.small),
            placeholder = debugPlaceholder()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = podcast.title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = podcast.publisher,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DiscoverBestPodcastsCompact(
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
