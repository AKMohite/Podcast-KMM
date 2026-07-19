package com.mak.pocketnotes.android.feature.discover.components.header

import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.window.core.layout.WindowSizeClass
import com.mak.pocketnotes.android.feature.discover.components.DiscoverCompactHeader
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isMedium
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun DiscoverHeader(
    podcasts: List<Podcast>,
    modifier: Modifier = Modifier,
    onPodcastClick: (String) -> Unit,
    sizeClass: WindowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
) {
    if (podcasts.isNotEmpty()) {
        when {
            sizeClass.isExpanded() -> DiscoverLargeHeader(
                podcasts = podcasts,
                modifier = modifier,
                onPodcastClick = onPodcastClick
            )

            sizeClass.isMedium() -> DiscoverMediumHeader(
                podcasts = podcasts,
                modifier = modifier,
                onPodcastClick = onPodcastClick
            )
            else -> DiscoverCompactHeader(
                podcasts = podcasts,
                modifier = modifier,
                onPodcastClick = onPodcastClick
            )
        }
    }
}

@Preview
@PreviewScreenSizes
@Composable
private fun DiscoverHeaderPreview() {
    PocketNotesTheme {
        Surface {
            DiscoverHeader(
                podcasts = samplePodcasts,
                onPodcastClick = {}
            )
        }
    }
}