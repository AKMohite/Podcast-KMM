package com.mak.pocketnotes.android.feature.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme

@Composable
internal fun PlayerHeader(
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
