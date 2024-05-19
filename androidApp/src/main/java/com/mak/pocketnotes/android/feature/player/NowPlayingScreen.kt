package com.mak.pocketnotes.android.feature.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.feature.player.components.PlaybackController
import com.mak.pocketnotes.android.feature.player.components.PlayerFooter
import com.mak.pocketnotes.android.feature.player.components.PlayerHeader
import com.mak.pocketnotes.android.feature.player.components.PlayerSlider
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.PlayableEpisode
import com.mak.pocketnotes.utils.sample.sampleEpisodes

@Composable
internal fun NowPlayingScreen(
    progress: Float,
    onCloseClick: () -> Unit,
    onSliderChange: (Float) -> Unit,
    playPause: () -> Unit,
    isMediaPLaying: Boolean,
    episode: PlayableEpisode,
    previousClick: () -> Unit,
    nextClick: () -> Unit
) {
    NowPlayingContent(
        episode = episode,
        onCloseClick = onCloseClick,
        progress = progress,
        onSliderChange = onSliderChange,
        playPause = playPause,
        isMediaPlaying = isMediaPLaying,
        previousClick = previousClick,
        nextClick = nextClick
    )
}

@Composable
private fun NowPlayingContent(
    episode: PlayableEpisode,
    onCloseClick: () -> Unit,
    progress: Float,
    onSliderChange: (Float) -> Unit,
    playPause: () -> Unit,
    isMediaPlaying: Boolean,
    previousClick: () -> Unit,
    nextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 6.dp)
    ) {
        PlayerHeader(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onCloseClick = onCloseClick,
            onMoreClick = {}
        )
        Spacer(modifier = Modifier.height(36.dp))
        AsyncImage(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(4.dp))
                .align(Alignment.CenterHorizontally),
            model = episode.image,
            contentDescription = episode.title,
            contentScale = ContentScale.Crop,
            placeholder = debugPlaceholder()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = episode.title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.weight(1f))
        PlayerSlider(
            durationRange = 0f..100f,
            onSliderChange = onSliderChange,
            currentProgress = progress
        )
        Spacer(modifier = Modifier.height(12.dp))
        PlaybackController(
            isMediaPlaying = isMediaPlaying,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onShuffleClick = {},
            playPause = playPause,
            previousClick = previousClick,
            nextClick = nextClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        PlayerFooter(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun NowPlayingPreview() {
    PocketNotesTheme {
        Surface {
            NowPlayingContent(
                episode = sampleEpisodes[0].asPlayableEpisode(),
                onCloseClick = {},
                progress = 20f,
                onSliderChange = {},
                playPause = {},
                isMediaPlaying = false,
                previousClick = {},
                nextClick = {}
            )
        }
    }
}