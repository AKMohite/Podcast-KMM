package com.mak.pocketnotes.android.feature.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme

@Composable
internal fun PlayerSlider(
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