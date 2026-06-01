package com.mak.pocketnotes.android.feature.discover.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.lerp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts
import kotlin.math.max

@Composable
fun DiscoverMediumHeader(
    podcasts: List<Podcast>,
    modifier: Modifier = Modifier,
    onPodcastClick: (String) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
        .padding(vertical = 16.dp)
    ) {
        val preferredWidth = maxWidth * 0.7f
        val state = rememberCarouselState(
            itemCount =  { podcasts.size }
        )

        HorizontalMultiBrowseCarousel(
            state = state,
            preferredItemWidth = preferredWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp), // Using a default height, could be from theme
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) { index ->
            val podcast = podcasts[index % podcasts.size]
            val info = carouselItemDrawInfo

            // Calculate progress of expansion (0f to 1f)
            // info.size is current size, info.maxSize is the expanded size, info.minSize is compact size
            val progress = if (info.maxSize - info.minSize > 0) {
                (info.size - info.minSize) / (info.maxSize - info.minSize)
            } else {
                1f
            }.coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .maskClip(MaterialTheme.shapes.extraLarge)
                    .clickable { onPodcastClick(podcast.id) }
            ) {
                AsyncImage(
                    model = podcast.image,
                    contentDescription = podcast.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = debugPlaceholder()
                )

                // Scrim for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                0.4f to Color.Transparent,
                                1f to Color.Black.copy(alpha = 0.8f)
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .graphicsLayer {
                            // Pin the text to the visible start of the item (the mask)
                            translationX = info.maskRect.left
                            alpha =
                                lerp(
                                    0f,
                                    1f,
                                    max(
                                        size.width - (carouselItemDrawInfo.maxSize) +
                                                carouselItemDrawInfo.size,
                                        0f,
                                    ) / size.width,
                                )
                        }
                        .padding(16.dp)
                ) {
                    val titleSize = lerp(MaterialTheme.typography.titleSmall, MaterialTheme.typography.displaySmall, progress)
                    val publisherSize = lerp(MaterialTheme.typography.labelSmall, MaterialTheme.typography.bodyMedium, progress)

                    Text(
                        text = podcast.title,
//                        style = MaterialTheme.typography.titleLarge.copy(
//                            fontSize = titleSize,
//                            fontWeight = FontWeight.Bold,
//                            lineHeight = titleSize * 1.2f
//                        ),
                        style = titleSize,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = podcast.publisher,
//                        style = MaterialTheme.typography.bodyMedium.copy(
//                            fontSize = publisherSize
//                        ),
                        style = publisherSize,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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
