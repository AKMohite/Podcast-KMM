package com.mak.pocketnotes.android.feature.discover.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun DiscoverLargeHeader(
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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .height(400.dp)
            .clip(RoundedCornerShape(32.dp))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val podcast = podcasts[page]
            LargePodcastCarouselCard(
                podcast = podcast,
                onPodcastClick = { onPodcastClick(podcast.id) }
            )
        }

        Row(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val size = if (pagerState.currentPage == iteration) 24.dp else 8.dp
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.4f)
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(color)
                        .size(height = 8.dp, width = size)
                )
            }
        }
    }
}

@Composable
private fun LargePodcastCarouselCard(
    podcast: Podcast,
    onPodcastClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
        .fillMaxSize()
        .clickable { onPodcastClick() }
    ) {
        AsyncImage(
            model = podcast.thumbnail,
            contentDescription = podcast.title,
            contentScale = ContentScale.Crop,
            placeholder = debugPlaceholder(),
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.inverseSurface,
                            MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.4f)
                        ),
                        startX = 0f,
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.6f)
                .padding(start = 48.dp, top = 48.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.Center
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.inverseOnSurface) {
                Text(
                    text = podcast.publisher,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.displayMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = podcast.description,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = onPodcastClick,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = stringResource(R.string.play_episodes),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.play_episodes),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        AsyncImage(
            model = podcast.image,
            placeholder = debugPlaceholder(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .padding(end = 48.dp, top = 48.dp, bottom = 48.dp)
                .align(Alignment.CenterEnd)
                .clip(MaterialTheme.shapes.medium)
        )
    }
}

@Preview(widthDp = 900)
@Composable
private fun DiscoverLargeHeaderPreview() {
    PocketNotesTheme {
        Surface {
            Box(Modifier.padding(24.dp)) {
                DiscoverLargeHeader(
                    podcasts = samplePodcasts,
                    onPodcastClick = {}
                )
            }
        }
    }
}
