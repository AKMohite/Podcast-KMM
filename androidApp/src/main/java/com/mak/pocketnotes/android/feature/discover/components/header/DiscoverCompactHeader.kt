package com.mak.pocketnotes.android.feature.discover.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.ui.theme.ThemePreviews
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun DiscoverCompactHeader(
    podcasts: List<Podcast>,
    modifier: Modifier = Modifier,
    onPodcastClick: (String) -> Unit
) {

    val pagerState = rememberPagerState(pageCount = { podcasts.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = pagerState.settledPage) {
        coroutineScope.launch {
            delay(5_500)
            val target =
                if (pagerState.currentPage == pagerState.pageCount - 1) 0 else pagerState.currentPage + 1
            pagerState.animateScrollToPage(target)
        }
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        HorizontalPager(
            state = pagerState
        ) { page ->
            DiscoverCarouselCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPodcastClick(podcasts[page].id) },
                podcast = podcasts[page]
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val size = if (pagerState.currentPage == iteration) 16.dp else 6.dp
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
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

@Composable
internal fun DiscoverCarouselCard(
    modifier: Modifier = Modifier,
    podcast: Podcast
) {
    val description = stringResource(R.string.podcast_card_description, podcast.title, podcast.publisher)
    Box(
        modifier = modifier
            .height(160.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = description
            }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = podcast.thumbnail,
                contentDescription = podcast.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(MaterialTheme.shapes.medium),
                placeholder = debugPlaceholder()
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = podcast.publisher,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = podcast.genres,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun DiscoverCompactHeaderPreview() {
    DiscoverCompactHeader(
        podcasts = samplePodcasts.take(8)
    ) {}
}