package com.mak.pocketnotes.android.feature.player.v2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.feature.player.v2.PlayerTestTags
import com.mak.pocketnotes.domain.models.PLAYBACK_SPEEDS
import com.mak.pocketnotes.domain.models.RepeatMode

@Composable
internal fun SecondaryControls(
    playbackSpeed: Float,
    isShuffleEnabled: Boolean,
    repeatMode: RepeatMode,
    onSetSpeed: (Float) -> Unit,
    onToggleShuffle: () -> Unit,
    onCycleRepeatMode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Speed button cycles through PLAYBACK_SPEEDS
        SpeedChip(speed = playbackSpeed, onSetSpeed = onSetSpeed)

        // Shuffle
        IconButton(
            onClick = onToggleShuffle,
            modifier = Modifier.testTag(PlayerTestTags.SHUFFLE_BUTTON),
        ) {
            Icon(
                Icons.Default.Shuffle,
                contentDescription = if (isShuffleEnabled) "Shuffle on" else "Shuffle off",
                tint = if (isShuffleEnabled) MaterialTheme.colorScheme.primary
                else LocalContentColor.current.copy(alpha = 0.5f),
            )
        }

        // Repeat
        IconButton(
            onClick = onCycleRepeatMode,
            modifier = Modifier.testTag(PlayerTestTags.REPEAT_BUTTON),
        ) {
            Icon(
                imageVector = when (repeatMode) {
                    RepeatMode.ONE -> Icons.Default.RepeatOne
                    else -> Icons.Default.Repeat
                },
                contentDescription = "Repeat mode: $repeatMode",
                tint = if (repeatMode != RepeatMode.NONE) MaterialTheme.colorScheme.primary
                else LocalContentColor.current.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
private fun SpeedChip(speed: Float, onSetSpeed: (Float) -> Unit) {
    val currentIndex = PLAYBACK_SPEEDS.indexOf(speed).takeIf { it >= 0 } ?: 2
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                val nextIndex = (currentIndex + 1) % PLAYBACK_SPEEDS.size
                onSetSpeed(PLAYBACK_SPEEDS[nextIndex])
            }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(PlayerTestTags.SPEED_BUTTON),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "${speed}x",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
        )
    }
}