package com.mak.pocketnotes.android.feature.discover.components.curatedpodcast

import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.window.core.layout.WindowSizeClass
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.utils.sample.sampleCuratedPodcasts

@Composable
internal fun DiscoverCuratedPodcasts(
    modifier: Modifier = Modifier,
    sizeClass: WindowSizeClass,
    podcastSection: CuratedPodcast,
    goToDetails: (String) -> Unit
) {
    when{
        sizeClass.isExpanded() -> DiscoverCuratedPodcastsExpanded(
            modifier,
            goToDetails,
            podcastSection
        )
        else -> DiscoverCuratedPodcastsCompactAndMedium(
            modifier,
            goToDetails,
            podcastSection,
            sizeClass
        )
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
                goToDetails = {},
                sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
            )
        }
    }
}
