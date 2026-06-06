package com.mak.pocketnotes.android.feature.player.v2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward30
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.feature.player.v2.PlayerEvent
import com.mak.pocketnotes.android.feature.player.v2.PlayerTestTags

@Composable
internal fun PlayerControls(
    isPlaying: Boolean,
    isLoading: Boolean,
    hasNext: Boolean,
    hasPrevious: Boolean,
    onEvent: (PlayerEvent) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
) {
    val iconSize = if (isCompact) 20.dp else 24.dp
    val playSize = if (isCompact) 48.dp else 64.dp

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Rewind 10 s
        IconButton(
            onClick = {
                onEvent(PlayerEvent.OnSkipBackward)
            },
            modifier = Modifier.testTag(PlayerTestTags.REWIND_BUTTON),
        ) {
            Icon(
                Icons.Default.Replay10,
                contentDescription = "Skip back 10 seconds",
                modifier = Modifier.size(iconSize + 4.dp),
            )
        }

        // Skip previous
        IconButton(
            onClick = {
                onEvent(PlayerEvent.OnSkipToPrevious)
            },
            enabled = hasPrevious,
            modifier = Modifier.testTag(PlayerTestTags.SKIP_PREV_BUTTON),
        ) {
            Icon(
                Icons.Default.SkipPrevious,
                contentDescription = "Previous episode",
                modifier = Modifier.size(iconSize + 4.dp),
            )
        }

        // Play / Pause (large)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(playSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        onEvent(PlayerEvent.OnTogglePlayPause)
                    },
                )
                .testTag(PlayerTestTags.PLAY_PAUSE_BUTTON)
                .semantics {
                    contentDescription = if (isPlaying) "Pause" else "Play"
                },
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(iconSize + 4.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(iconSize + 8.dp),
                )
            }
        }

        // Skip next
        IconButton(
            onClick = {
                onEvent(PlayerEvent.OnSkipToNext)
            },
            enabled = hasNext,
            modifier = Modifier.testTag(PlayerTestTags.SKIP_NEXT_BUTTON),
        ) {
            Icon(
                Icons.Default.SkipNext,
                contentDescription = "Next episode",
                modifier = Modifier.size(iconSize + 4.dp),
            )
        }

        // Forward 30 s
        IconButton(
            onClick = {
                onEvent(PlayerEvent.OnSkipForward)
            },
            modifier = Modifier.testTag(PlayerTestTags.FORWARD_BUTTON),
        ) {
            Icon(
                Icons.Default.Forward30,
                contentDescription = "Skip forward 30 seconds",
                modifier = Modifier.size(iconSize + 4.dp),
            )
        }
    }
}