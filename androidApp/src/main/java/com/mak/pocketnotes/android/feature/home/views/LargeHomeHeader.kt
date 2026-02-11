package com.mak.pocketnotes.android.feature.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun LargeHomeHeader(
    modifier: Modifier = Modifier,
    podcasts: List<Podcast>,
    onPodcastClick: (String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { podcasts.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = pagerState.settledPage) {
        launch {
            delay(5_500)
            val target =
                if (pagerState.currentPage == pagerState.pageCount - 1) 0 else pagerState.currentPage + 1
            pagerState.animateScrollToPage(target)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val podcastDets = podcasts[pagerState.currentPage]
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize(),
                state = pagerState
            ) { page ->
                val podcast = podcasts[page]
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        onPodcastClick(podcast.id)
                    },
                    contentAlignment = Alignment.CenterEnd) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
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
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .align(Alignment.Center),
                verticalAlignment = Alignment.Bottom
            ) {
                Spacer(modifier = Modifier.size(180.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = podcastDets.title,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = podcastDets.publisher,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = podcastDets.genres,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = podcastDets.description,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Thin),
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(8.dp))
                    Row(
                        Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val size = if (pagerState.currentPage == iteration) 20.dp else 12.dp
                            val color =
                                if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
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
        }


    }
}

@Preview
@Composable
private fun LargeHomeHeaderPreview() {
    PocketNotesTheme {
        LargeHomeHeader(
            podcasts = samplePodcasts.take(8)
        ) {}
    }
}