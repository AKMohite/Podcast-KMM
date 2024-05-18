package com.mak.pocketnotes.android.feature.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.common.ui.debugPlaceholder
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.utils.sample.sampleEpisodes

@Composable
internal fun NowPlayingScreen() {
    NowPlayingContent(sampleEpisodes[0])
}

@Composable
private fun NowPlayingContent(
    episode: PodcastEpisode
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
            onCloseClick = {},
            onMoreClick = {}
        )
        Spacer(modifier = Modifier.height(36.dp))
        AsyncImage(
            modifier = Modifier
                .size(330.dp)
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
            onSliderChange = {},
        )
        Spacer(modifier = Modifier.height(12.dp))
        PlaybackController(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            onShuffleClick = {}
        )
        Spacer(modifier = Modifier.height(8.dp))
        PlayerFooter(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PlayerFooter(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {  }) {
//            TODO change drawable
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = stringResource(R.string.player_available_device)
            )
        }
        IconButton(onClick = {  }) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.player_share)
            )
        }
    }
}

@Composable
private fun PlaybackController(
    modifier: Modifier = Modifier,
    onShuffleClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onShuffleClick) {
//            TODO change drawable
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = stringResource(R.string.player_shuffle)
            )
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.player_previous)
            )
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = stringResource(R.string.player_play_pause)
            )
        }
        IconButton(onClick = {  }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.player_next)
            )
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = stringResource(R.string.player_favorite)
            )
        }
    }
}

@Composable
private fun PlayerSlider(
    modifier: Modifier = Modifier,
    currentProgress: Float = 0f,
    timeElapsed: String = "00:00",
    totalDuration: String = "",
    durationRange: ClosedFloatingPointRange<Float>,
    onSliderChange: (Float) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Slider(
            modifier = Modifier
                .fillMaxWidth(),
            value = currentProgress,
            valueRange = durationRange,
            onValueChange = onSliderChange
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = timeElapsed,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = totalDuration,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun PlayerHeader(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onCloseClick) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = stringResource(R.string.close_full_player)
            )
        }
        Text(
            text = stringResource(R.string.now_playing),
            style = MaterialTheme.typography.headlineSmall
        )
        IconButton(onClick = onMoreClick) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = stringResource(R.string.more_full_player)
            )
        }

    }
}


@Preview
@Composable
private fun PlayerHeaderPreview() {
    PocketNotesTheme {
        Surface {
            PlayerHeader(
                onCloseClick = {},
                onMoreClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun PlayerSliderPreview() {
    PocketNotesTheme {
        Surface {
            PlayerSlider(
                currentProgress = 30f,
                durationRange = 0f..100f,
                onSliderChange = {},
                totalDuration = "04:57"
            )
        }
    }
}

@Preview
@Composable
private fun PlayerFooterPreview() {
    PocketNotesTheme {
        Surface {
            PlayerFooter()
        }
    }
}

@Preview
@Composable
private fun PlaybackControllerPreview() {
    PocketNotesTheme {
        Surface {
            PlaybackController(
                onShuffleClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun NowPlayingPreview() {
    PocketNotesTheme {
        Surface {
            NowPlayingContent(sampleEpisodes[0])
        }
    }
}