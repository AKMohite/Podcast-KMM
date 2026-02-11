package com.mak.pocketnotes.android.feature.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.common.navigation.AdaptiveScreenType
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    val pagerState = rememberPagerState(pageCount = { podcasts.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = pagerState.settledPage) {
        launch {
            delay(5_500)
            val target = if (pagerState.currentPage == pagerState.pageCount - 1) 0 else pagerState.currentPage + 1
            pagerState.animateScrollToPage(target)
        }
    }

    Column(
        modifier = modifier
    ) {
        HorizontalPager(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer),
            state = pagerState
        ) { page ->
            HomeCarouselCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPodcastClick(podcasts[page].id) },
                podcast = podcasts[page]
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val size = if (pagerState.currentPage == iteration) 12.dp else 6.dp
                val color = if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(iteration)
                            }
                        }
                        .background(color)
                        .size(height = 6.dp, width = size)
                )
            }
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