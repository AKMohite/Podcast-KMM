package com.mak.pocketnotes.android.feature.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.ui.theme.ThemePreviews

@Composable
internal fun PlaybackController(
    isMediaPlaying: Boolean,
    modifier: Modifier = Modifier,
    onShuffleClick: () -> Unit,
    playPause: () -> Unit,
    previousClick: () -> Unit,
    nextClick: () -> Unit,
    backwardClick: () -> Unit = {},
    forwardClick: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
//        IconButton(onClick = onShuffleClick) {
////            TODO change drawable
//            Icon(
//                imageVector = Icons.Rounded.Refresh,
//                contentDescription = stringResource(R.string.player_shuffle)
//            )
//        }
        IconButton(onClick = backwardClick) {
            Icon(
                painter = painterResource(R.drawable.icon_previous_ten),
                contentDescription = stringResource(R.string.player_backward)
            )
        }
        IconButton(onClick = previousClick) {
            Icon(
                painter = painterResource(R.drawable.icon_previous),
                contentDescription = stringResource(R.string.player_previous)
            )
        }
        FilledIconButton(onClick = playPause) {
            Icon(
                imageVector = if (isMediaPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = stringResource(R.string.player_play_pause)
            )
        }
        IconButton(onClick = nextClick) {
            Icon(
                painter = painterResource(R.drawable.icon_next),
                contentDescription = stringResource(R.string.player_next)
            )
        }
        IconButton(onClick = forwardClick) {
            Icon(
                painter = painterResource(R.drawable.icon_forward_ten),
                contentDescription = stringResource(R.string.player_forward)
            )
        }
//        IconButton(onClick = {}) {
//            Icon(
//                imageVector = Icons.Rounded.FavoriteBorder,
//                contentDescription = stringResource(R.string.player_favorite)
//            )
//        }
    }
}

@ThemePreviews
@Composable
private fun PlaybackControllerPreview() {
    PlaybackController(
        isMediaPlaying = false,
        onShuffleClick = {},
        playPause = {},
        previousClick = {},
        nextClick = {}
    )
}
