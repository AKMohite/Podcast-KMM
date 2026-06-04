package com.mak.pocketnotes.android.feature.discover.components.curatedpodcast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.feature.discover.components.bestpodcast.PodcastColumn
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.SectionPodcast

@Composable
internal fun DiscoverCuratedPodcastsExpanded(
    modifier: Modifier = Modifier,
    goToDetails: (String) -> Unit,
    podcastSection: CuratedPodcast,
) {
    Column(
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(MaterialTheme.shapes.large),
            color = MaterialTheme.colorScheme.inverseSurface,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 32.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = podcastSection.title,
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = podcastSection.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { /* Do something! */ }) {
                        Text(stringResource(R.string.see_all))
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.see_all),
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .width(320.dp)
                        .fillMaxHeight()
                        .clipToBounds()
                ) {
                    CuratedSlantedGrid(
                        images = podcastSection.getSectionImages(),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = 50.dp)
                    )
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            val spacing = 12.dp
            val itemWidth = (maxWidth - (spacing * 4)) / 5
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                items(items = podcastSection.podcasts, key = SectionPodcast::id) { podcast ->
                    PodcastColumn(
                        modifier = Modifier
                            .width(itemWidth)
                            .clickable { goToDetails(podcast.id) },
                        title = podcast.title,
                        publisher = podcast.publisher,
                        image = podcast.thumbnail
                    )
                }
            }
        }
    }
}

@Composable
private fun CuratedSlantedGrid(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                rotationZ = 20f
                rotationY = 10f
                scaleX = 1.5f
                scaleY = 1.5f
            }
    ) {
        Column {
            val rows = 3
            val cols = 3
            for (r in 0 until rows) {
                Row {
                    for (c in 0 until cols) {
                        val index = r * cols + c
                        if (index < images.size) {
                            AsyncImage(
                                model = images[index],
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp),
                                contentScale = ContentScale.Crop,
                                placeholder = debugPlaceholder()
                            )
                        }
                    }
                }
            }
        }
    }
}