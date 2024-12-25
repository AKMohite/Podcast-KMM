package com.mak.pocketnotes.android.feature.home.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.feature.home.HomeScreenState
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
private fun NotUsed(
    uiState: HomeScreenState,
    loadNextPodcasts: (Boolean) -> Unit,
    gotoDetails: (Podcast) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            uiState.podcasts,
            key = { _, podcast: Podcast -> podcast.id }
        ) { index: Int, podcast: Podcast ->
            PodcastItem(podcast = podcast, gotoDetails = gotoDetails)
            if (index >= uiState.podcasts.size - 1 && !uiState.loading && !uiState.loadFinished) {
                LaunchedEffect(key1 = Unit, block = { loadNextPodcasts(false) })
            }
        }

        if (uiState.loading && uiState.podcasts.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
internal fun PodcastItem(
    modifier: Modifier = Modifier,
    podcast: Podcast,
    gotoDetails: (Podcast) -> Unit
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .height(220.dp)
            .clickable { gotoDetails(podcast) },
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = podcast.thumbnail,
                    contentDescription = podcast.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.small.copy(bottomStart = CornerSize(2.dp), bottomEnd = CornerSize(2.dp))),
                    placeholder = debugPlaceholder()

                )
//                Surface(
//                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
//                    modifier = Modifier
//                        .size(50.dp)
//                        .clip(CircleShape)
//                        .padding(12.dp)
//                ) {
//                    Image(painter = painterResource(id = coil.base.R.drawable.notification_icon_background), contentDescription = )
//                }
            }

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = podcast.publisher,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun PodcastItemPreview() {
    PocketNotesTheme {
        PodcastItem(
            podcast = samplePodcasts[0],
            gotoDetails = {}
        )
    }
}
