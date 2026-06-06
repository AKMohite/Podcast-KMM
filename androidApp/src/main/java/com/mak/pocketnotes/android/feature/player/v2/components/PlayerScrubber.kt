package com.mak.pocketnotes.android.feature.player.v2.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mak.pocketnotes.android.feature.player.v2.PlayerTestTags

@Composable
internal fun PlayerScrubber(
    positionMs: Long,
    durationMs: Long,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Local drag state — don't update positionMs while user is dragging
    var isDragging by remember { mutableStateOf(false) }
    var dragValue by remember { mutableFloatStateOf(0f) }

    val displayProgress = if (isDragging) dragValue
    else if (durationMs > 0L) positionMs.toFloat() / durationMs.toFloat()
    else 0f

    Column(modifier = modifier) {
        Slider(
            value = displayProgress,
            onValueChange = { value ->
                isDragging = true
                dragValue = value
            },
            onValueChangeFinished = {
                onSeekTo((dragValue * durationMs).toLong())
                isDragging = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(PlayerTestTags.SCRUBBER),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = formatDuration(positionMs),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatDuration(durationMs),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}