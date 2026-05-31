package com.mak.pocketnotes.android.feature.discover.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts
import kotlin.math.absoluteValue

@Composable
fun DiscoverMediumHeader(
    podcasts: List<Podcast>,
    modifier: Modifier = Modifier,
    onPodcastClick: (String) -> Unit
) {
    val startPage = Int.MAX_VALUE / 2

    val pagerState = rememberPagerState(
        initialPage = startPage,
        pageCount = { Int.MAX_VALUE }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 64.dp),
            pageSpacing = 16.dp,
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) { page ->

            val podcast = podcasts[page % podcasts.size]

            val pageOffset by remember(page) {
                derivedStateOf {
                    (
                            (pagerState.currentPage - page) +
                                    pagerState.currentPageOffsetFraction
                            ).absoluteValue
                }
            }

            Card(
                modifier = Modifier
                    .graphicsLayer {
                        val scale = lerp(
                            start = 0.9f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        val alpha = lerp(
                            start = 0.7f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        val rotation = lerp(
                            start = 12f,
                            stop = 0f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                        val offset = (
                                (pagerState.currentPage - page)
                                        + pagerState.currentPageOffsetFraction
                                )

                        translationX = offset * 120f

                        scaleX = scale
                        scaleY = scale

                        this.alpha = alpha

//                        scaleX = scale
//                        scaleY = scale
//
//                        this.alpha = alpha
//
//                        rotationZ =
//                            if (page < pagerState.currentPage)
//                                rotation
//                            else
//                                -rotation
                    }
                    .fillMaxWidth()
                    .height(400.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {

                Box {

                    AsyncImage(
                        model = podcast.image,
                        contentDescription = podcast.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                            .graphicsLayer {
                                translationX = pageOffset * 80f
                            }
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                    ) {
                        Text(
                            text = podcast.title,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }
            }
        }
    }

}

@Preview(widthDp = 600)
@Composable
private fun DiscoverMediumHeaderPreview() {
    PocketNotesTheme {
        Surface {
            DiscoverMediumHeader(
                podcasts = samplePodcasts,
                onPodcastClick = {}
            )
        }
    }
}
