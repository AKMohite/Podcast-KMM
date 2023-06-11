package com.mak.pocketnotes.android.feature.podcastdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.feature.podcastdetail.views.PodcastEpisodeItem
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.PodcastEpisode
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun PodcastDetailScreen(
    movieId: String
) {
    val detailViewModel: PodcastDetailViewModel = koinViewModel(
        parameters = { parametersOf(movieId) }
    )
    PodcastDetailContent(
        uiState = detailViewModel.uiState
    )
}

@Composable
private fun PodcastDetailContent(
    modifier: Modifier = Modifier,
    uiState: PodcastDetailState
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        uiState.podcast?.let { podcast ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AsyncImage(
                    model = podcast.image,
                    contentDescription = podcast.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(20.dp)
                ) {
                    Text(
                        text = podcast.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Start listening now",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "By: ${podcast.publisher}".uppercase(),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = podcast.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                LazyColumn {
                    items(items = podcast.episodes, key = { episode: PodcastEpisode -> episode.id }) { episode ->
                        PodcastEpisodeItem(episode = episode)
                    }
                }
            }
        }
        if (uiState.loading) {
            Row(
                modifier = modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun PodcastDetailScreenPreview() {
    PocketNotesTheme {
        PodcastDetailContent(uiState = PodcastDetailState())
    }
}
