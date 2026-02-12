package com.mak.pocketnotes.android.feature.home.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.common.navigation.AdaptiveScreenType
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts
import kotlinx.coroutines.launch

@Composable
internal fun HomeCarousel(
    modifier: Modifier = Modifier,
    podcasts: List<Podcast>,
    onPodcastClick: (String) -> Unit
) {

    val carouselState = rememberCarouselState { podcasts.size }
    val animationScope = rememberCoroutineScope()

    HorizontalCenteredHeroCarousel(
        state = carouselState,
        modifier = modifier
            .height(250.dp)
            .padding(horizontal = 12.dp),
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { index ->
        HomeCarouselCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(true, "Click to focus", Role.Image) {
                    animationScope.launch {
                        carouselState.animateScrollToItem(index)
                    }
                },
//                .clickable { onPodcastClick(podcasts[index].id) },
            podcast = podcasts[index]
        )
    }
}

@Composable
internal fun HomeHeader(
    modifier: Modifier = Modifier,
    adaptiveScreenType: AdaptiveScreenType = AdaptiveScreenType.Compact,
    podcasts: List<Podcast>,
    onPodcastClick: (String) -> Unit
) {
    if (podcasts.isNotEmpty()) {
        when(adaptiveScreenType) {
            AdaptiveScreenType.Compact -> CompactHomeHeader(podcasts = podcasts, modifier = modifier, onPodcastClick = onPodcastClick)
            AdaptiveScreenType.Medium -> MediumHomeHeader(podcasts = podcasts, modifier = modifier, onPodcastClick = onPodcastClick)
            else -> LargeHomeHeader(podcasts = podcasts, modifier = modifier, onPodcastClick = onPodcastClick)
        }
    }
}

@Preview
@Composable
private fun HomeHeaderPreview() {
    PocketNotesTheme {
        Surface {
            HomeHeader(
                modifier = Modifier.fillMaxWidth(),
                podcasts = samplePodcasts.take(5),
                onPodcastClick = {},
            )
        }
    }
}