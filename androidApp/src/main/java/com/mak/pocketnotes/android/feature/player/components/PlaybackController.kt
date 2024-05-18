package com.mak.pocketnotes.android.feature.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme

@Composable
internal fun PlaybackController(
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
        IconButton(onClick = { }) {
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
