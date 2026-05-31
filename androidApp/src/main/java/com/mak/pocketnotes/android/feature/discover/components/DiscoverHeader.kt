package com.mak.pocketnotes.android.feature.discover.components

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isMedium
import com.mak.pocketnotes.domain.models.Podcast

@Composable
internal fun DiscoverHeader(
    podcasts: List<Podcast>,
    modifier: Modifier = Modifier,
    onPodcastClick: (String) -> Unit,
    sizeClass: WindowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
) {
    if (podcasts.isNotEmpty()) {
        when {
            sizeClass.isExpanded() -> DiscoverLargeHeader(podcasts = podcasts, modifier = modifier, onPodcastClick = onPodcastClick)
            sizeClass.isMedium() -> DiscoverMediumHeader(podcasts = podcasts, modifier = modifier, onPodcastClick = onPodcastClick)
            else -> DiscoverCompactHeader(podcasts = podcasts, modifier = modifier, onPodcastClick = onPodcastClick)
        }
    }
}

@Composable
fun DiscoverLargeHeader(
    podcasts: List<Podcast>,
    modifier: Modifier,
    onPodcastClick: (String) -> Unit
) {
    DiscoverCompactHeader(podcasts = podcasts, modifier = modifier, onPodcastClick = onPodcastClick)
}