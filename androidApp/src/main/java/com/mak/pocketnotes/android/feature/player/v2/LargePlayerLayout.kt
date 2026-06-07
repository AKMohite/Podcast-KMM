package com.mak.pocketnotes.android.feature.player.v2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.feature.player.v2.components.NowPlayingArtwork
import com.mak.pocketnotes.android.feature.player.v2.components.NowPlayingInfo
import com.mak.pocketnotes.android.feature.player.v2.components.PlayerControls
import com.mak.pocketnotes.android.feature.player.v2.components.PlayerScrubber
import com.mak.pocketnotes.android.feature.player.v2.components.QueuePanel
import com.mak.pocketnotes.android.feature.player.v2.components.SecondaryControls
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.RepeatMode
import com.mak.pocketnotes.utils.sample.sampleEpisodes

// ─────────────────────────────────────────────────────────────────────────────
// LARGE  1200–1599 dp  ·  Large tablet / foldable outer
//
//  ┌──────────────────────────────────────────────────────────────┐
//  │  Player (0.5)           │  Queue (0.5)                       │
//  │  [Artwork 280dp]        │  Up Next ─────────────────────     │
//  │  Title/Podcast          │  Ep 1 (now playing)                │
//  │  Scrubber               │  Ep 2                              │
//  │  Controls               │  Ep 3                              │
//  └──────────────────────────────────────────────────────────────┘
// ─────────────────────────────────────────────────────────────────────────────
@Composable
internal fun LargePlayerLayout(
    state: PlayerState,
    onEvent: (PlayerEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        // Player pane
        Column(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            NowPlayingArtwork(
                artworkUrl = state.currentEpisode?.thumbnail,
                isPlaying = state.isPlaying,
                size = 280.dp,
            )
            Spacer(Modifier.height(24.dp))
            NowPlayingInfo(episode = state.currentEpisode, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            PlayerScrubber(
                positionMs = state.positionMs,
                durationMs = state.durationMs,
                onSeekTo = { onEvent(PlayerEvent.OnSeekTo(it)) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            PlayerControls(
                isPlaying = state.isPlaying,
                isLoading = state.isLoading,
                hasNext = state.hasNext,
                hasPrevious = state.hasPrevious,
                onEvent = onEvent
            )
            Spacer(Modifier.height(8.dp))
            SecondaryControls(
                playbackSpeed = state.playbackSpeed,
                isShuffleEnabled = state.isShuffleEnabled,
                repeatMode = state.repeatMode,
                onSetSpeed = { onEvent(PlayerEvent.OnSetSpeed) },
                onToggleShuffle = { onEvent(PlayerEvent.OnToggleShuffle) },
                onCycleRepeatMode = { onEvent(PlayerEvent.OnCycleRepeatMode) },
            )
        }

        // Queue pane
        QueuePanel(
            state = state,
            onEvent = onEvent,
            modifier = Modifier
                .weight(0.5f)
                .fillMaxHeight(),
        )
    }
}

@Preview(widthDp = 1300)
@Composable
private fun LargePlayerLayoutPreview() {
    PocketNotesTheme {
        Surface {
            LargePlayerLayout(
                state = PlayerState(
                    currentEpisode = sampleEpisodes[0],
                    queue = sampleEpisodes,
                    currentQueueIndex = 0,
                    isPlaying = true,
                    isLoading = false,
                    positionMs = 50_000L,
                    durationMs = sampleEpisodes[0].duration.toLong() * 1000L,
                    playbackSpeed = 1.0f,
                    isShuffleEnabled = false,
                    repeatMode = RepeatMode.NONE
                ),
                onEvent = {}
            )
        }
    }
}