package com.mak.pocketnotes.android.feature.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.mak.pocketnotes.android.ui.theme.adaptiveScreenInfo
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.domain.models.PlayableEpisode
import com.mak.pocketnotes.utils.sample.sampleEpisodes


@Composable
internal fun NowPlayingScreen(
    progress: Float,
    onCloseClick: () -> Unit,
    onSliderChange: (Float) -> Unit,
    playPause: () -> Unit,
    isMediaPlaying: Boolean,
    episode: PlayableEpisode,
    previousClick: () -> Unit,
    nextClick: () -> Unit,
    backwardClick: () -> Unit,
    forwardClick: () -> Unit,
    timeElapsed: String,
    totalDuration: String
) {
    val adaptiveInfo = adaptiveScreenInfo()
    val isExpanded = adaptiveInfo.windowSizeClass.isExpanded()

    if (isExpanded) {
        NowPlayingExpanded(
            episode = episode,
            onCloseClick = onCloseClick,
            progress = progress,
            onSliderChange = onSliderChange,
            playPause = playPause,
            isMediaPlaying = isMediaPlaying,
            previousClick = previousClick,
            nextClick = nextClick,
            backwardClick = backwardClick,
            forwardClick = forwardClick,
            timeElapsed = timeElapsed,
            totalDuration = totalDuration
        )
    } else {
        NowPlayingCompact(
            episode = episode,
            onCloseClick = onCloseClick,
            progress = progress,
            onSliderChange = onSliderChange,
            playPause = playPause,
            isMediaPlaying = isMediaPlaying,
            previousClick = previousClick,
            nextClick = nextClick,
            backwardClick = backwardClick,
            forwardClick = forwardClick,
            timeElapsed = timeElapsed,
            totalDuration = totalDuration
        )
    }
}

@Composable
private fun NowPlayingCompact(
    episode: PlayableEpisode,
    onCloseClick: () -> Unit,
    progress: Float,
    onSliderChange: (Float) -> Unit,
    playPause: () -> Unit,
    isMediaPlaying: Boolean,
    previousClick: () -> Unit,
    nextClick: () -> Unit,
    backwardClick: () -> Unit,
    forwardClick: () -> Unit,
    timeElapsed: String,
    totalDuration: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                .size(350.dp)
                .clip(MaterialTheme.shapes.extraSmall),
            model = episode.image,
            contentDescription = episode.title,
            contentScale = ContentScale.Crop,
            placeholder = debugPlaceholder()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = episode.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = episode.speaker,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(32.dp))
        PlayerSlider(
            durationRange = 0f..100f,
            onSliderChange = onSliderChange,
            currentProgress = progress,
            timeElapsed = timeElapsed,
            totalDuration = totalDuration
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
            nextClick = nextClick,
            backwardClick = backwardClick,
            forwardClick = forwardClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        PlayerFooter(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun NowPlayingExpanded(
    episode: PlayableEpisode,
    onCloseClick: () -> Unit,
    progress: Float,
    onSliderChange: (Float) -> Unit,
    playPause: () -> Unit,
    isMediaPlaying: Boolean,
    previousClick: () -> Unit,
    nextClick: () -> Unit,
    backwardClick: () -> Unit,
    forwardClick: () -> Unit,
    timeElapsed: String,
    totalDuration: String
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.extraSmall),
            model = episode.image,
            contentDescription = episode.title,
            contentScale = ContentScale.Crop,
            placeholder = debugPlaceholder()
        )
        Spacer(modifier = Modifier.width(32.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            PlayerHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onCloseClick = onCloseClick,
                onMoreClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = episode.title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = episode.speaker,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(32.dp))
            PlayerSlider(
                durationRange = 0f..100f,
                onSliderChange = onSliderChange,
                currentProgress = progress,
                timeElapsed = timeElapsed,
                totalDuration = totalDuration
            )
            Spacer(modifier = Modifier.height(24.dp))
            PlaybackController(
                isMediaPlaying = isMediaPlaying,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onShuffleClick = {},
                playPause = playPause,
                previousClick = previousClick,
                nextClick = nextClick,
                backwardClick = backwardClick,
                forwardClick = forwardClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            PlayerFooter(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun NowPlayingPreview() {
    PocketNotesTheme {
        Surface {
            NowPlayingScreen(
                episode = sampleEpisodes[0].asPlayableEpisode(),
                onCloseClick = {},
                progress = 20f,
                onSliderChange = {},
                playPause = {},
                isMediaPlaying = false,
                previousClick = {},
                nextClick = {},
                backwardClick = {},
                forwardClick = {},
                timeElapsed = "03:39",
                totalDuration = "14:40"
            )
        }
    }
}