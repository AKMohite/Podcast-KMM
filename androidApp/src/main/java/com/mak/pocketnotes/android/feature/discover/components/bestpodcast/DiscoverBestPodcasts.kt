package com.mak.pocketnotes.android.feature.discover.components.bestpodcast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
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
internal fun PodcastColumn(
    title: String,
    publisher: String,
    image: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            model = image,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.small),
            placeholder = debugPlaceholder()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = publisher,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
