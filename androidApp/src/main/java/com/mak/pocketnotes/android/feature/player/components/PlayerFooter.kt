package com.mak.pocketnotes.android.feature.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme

@Composable
internal fun PlayerFooter(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.icon_speed),
                contentDescription = stringResource(R.string.player_speed)
            )
        }

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.icon_playlist_queue),
                contentDescription = stringResource(R.string.player_queue)
            )
        }

        IconButton(onClick = { }) {
//            TODO change drawable
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = stringResource(R.string.player_available_device)
            )
        }
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.player_share)
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