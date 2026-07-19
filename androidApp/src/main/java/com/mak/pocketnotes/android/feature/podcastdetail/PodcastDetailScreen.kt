package com.mak.pocketnotes.android.feature.podcastdetail

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.window.core.layout.WindowSizeClass
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.common.PodcastDetail
import com.mak.pocketnotes.android.common.navigation.Navigator
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.feature.discover.components.PodcastRow
import com.mak.pocketnotes.android.feature.player.v2.PlayerEvent
import com.mak.pocketnotes.android.feature.player.v2.PlayerViewModel
import com.mak.pocketnotes.android.feature.podcastdetail.views.PodcastEpisodeItem
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.ui.theme.adaptiveScreenInfo
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isLarge
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.utils.sample.samplePodcasts
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


fun EntryProviderScope<NavKey>.podcastDetailEntry(
    navigator: Navigator
) {
    entry<PodcastDetail> { key ->
        val detailViewModel: PodcastDetailViewModel = koinViewModel(
            parameters = { parametersOf(key.podcastId) }
        )
        val playerViewModel: PlayerViewModel = koinViewModel(
            viewModelStoreOwner = LocalActivity.current as ComponentActivity
        )
        val state by detailViewModel.uiState.collectAsStateWithLifecycle()
        PodcastDetailScreen(
            state = state,
            episodes = state.episodes,
            startPodcast = {
                playerViewModel.onEvent(PlayerEvent.OnPlayQueue(state.episodes.take(10)))
//                startPodcastEpisodes(detailViewModel.episodesState)
            },
            gotoDetails = { podcastId ->
                navigator.navigate(PodcastDetail(podcastId))
            }
        )
    }
}

@Composable
internal fun PodcastDetailScreen(
    state: PodcastDetailState,
    episodes: List<PodcastEpisode>,
    startPodcast: () -> Unit,
    gotoDetails: (String) -> Unit
) {
    PodcastDetailContent(
        uiState = state,
        startPodcast = startPodcast,
        episodes = episodes,
        gotoDetails = gotoDetails
    )
}

@Composable
private fun PodcastDetailContent(
    modifier: Modifier = Modifier,
    uiState: PodcastDetailState,
    episodes: List<PodcastEpisode>,
    gotoDetails: (String) -> Unit,
    startPodcast: () -> Unit
) {
    val sizeClass = adaptiveScreenInfo().windowSizeClass
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        uiState.podcast?.let { podcast ->
            if (sizeClass.isExpanded()) {
                PodcastDetailExpanded(
                    podcast = podcast,
                    episodes = episodes,
                    gotoDetails = gotoDetails,
                    startPodcast = startPodcast,
                    sizeClass = sizeClass
                )
            } else {
                PodcastDetailCompact(
                    podcast = podcast,
                    episodes = episodes,
                    gotoDetails = gotoDetails,
                    startPodcast = startPodcast
                )
            }
        }
        if (uiState.loading) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun PodcastDetailCompact(
    podcast: Podcast,
    episodes: List<PodcastEpisode>,
    gotoDetails: (String) -> Unit,
    startPodcast: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item(key = "poster-image") {
            AsyncImage(
                placeholder = debugPlaceholder(),
                model = podcast.image,
                contentDescription = podcast.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
        item(key = "podcast-overview") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = podcast.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = startPodcast,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.start_listening_now)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "By: ${podcast.publisher}".uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = podcast.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        item(key = "podcast-recommendations-title") {
            if (podcast.recommendations.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    text = stringResource(R.string.reccomendations),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        item(key = "podcast-recommendations") {
            if (podcast.recommendations.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = podcast.recommendations,
                        key = { recommendation: Podcast -> recommendation.id }
                    ) { recommendation ->
                        PodcastRow(
                            modifier = Modifier
                                .clickable { gotoDetails(recommendation.id) }
                                .width(300.dp),
                            podcast = recommendation
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        item(key = "episodes-title") {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                text = stringResource(R.string.podcast_episodes),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        items(
            items = episodes,
            key = { episode: PodcastEpisode -> episode.id }
        ) { episode ->
            PodcastEpisodeItem(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                episode = episode
            )
        }
    }
}

@Composable
private fun PodcastDetailExpanded(
    podcast: Podcast,
    episodes: List<PodcastEpisode>,
    gotoDetails: (String) -> Unit,
    startPodcast: () -> Unit,
    sizeClass: WindowSizeClass
) {
    val isLarge = sizeClass.isLarge()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = if (isLarge) 40.dp else 24.dp),
        horizontalArrangement = Arrangement.spacedBy(if (isLarge) 40.dp else 24.dp)
    ) {
        // Main Pane: Podcast Header and Episodes
        LazyColumn(
            modifier = Modifier
                .weight(if (isLarge) 0.7f else 0.65f)
                .fillMaxHeight(),
            contentPadding = PaddingValues(vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    AsyncImage(
                        model = podcast.image,
                        contentDescription = podcast.title,
                        modifier = Modifier
                            .size(if (isLarge) 240.dp else 180.dp)
                            .clip(MaterialTheme.shapes.large),
                        contentScale = ContentScale.Crop,
                        placeholder = debugPlaceholder()
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = podcast.title,
                            style = if (isLarge) MaterialTheme.typography.displaySmall else MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = podcast.publisher.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = startPodcast,
                            shape = MaterialTheme.shapes.medium,
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.start_listening_now))
                        }
                    }
                }
            }
            
            item {
                Column {
                    Text(
                        text = stringResource(R.string.podcast_about),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = podcast.description,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp
                    )
                }
            }

            item {
                Text(
                    text = stringResource(R.string.podcast_episodes),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(episodes, key = { it.id }) { episode ->
                PodcastEpisodeItem(
                    episode = episode,
                    showImage = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Supporting Pane: Recommendations
        Column(
            modifier = Modifier
                .weight(if (isLarge) 0.3f else 0.35f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.reccomendations),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(podcast.recommendations, key = { it.id }) { recommendation ->
                    PodcastRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { gotoDetails(recommendation.id) }
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(8.dp),
                        podcast = recommendation
                    )
                }
            }
        }
    }
}

@Preview
@PreviewScreenSizes
@Composable
private fun PodcastDetailScreenPreview() {
    PocketNotesTheme {
        PodcastDetailContent(
            uiState = PodcastDetailState(
                podcast = samplePodcasts[0].copy(recommendations = samplePodcasts)
            ),
            episodes = samplePodcasts.flatMap { it.episodes },
            gotoDetails = {},
            startPodcast = {}
        )
    }
}
