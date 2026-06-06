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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.feature.player.v2.components.EpisodeInfoPanel
import com.mak.pocketnotes.android.feature.player.v2.components.NowPlayingArtwork
import com.mak.pocketnotes.android.feature.player.v2.components.NowPlayingInfo
import com.mak.pocketnotes.android.feature.player.v2.components.PlayerControls
import com.mak.pocketnotes.android.feature.player.v2.components.PlayerScrubber
import com.mak.pocketnotes.android.feature.player.v2.components.QueuePanel
import com.mak.pocketnotes.android.feature.player.v2.components.SecondaryControls

// ─────────────────────────────────────────────────────────────────────────────
// EXTRA LARGE  ≥ 1600 dp  ·  Desktop / foldable fully open
//
//  ┌──────────────────────────────────────────────────────────────────────────┐
//  │  Player (0.4)         │  Queue (0.35)        │  Episode info (0.25)     │
//  └──────────────────────────────────────────────────────────────────────────┘
// ─────────────────────────────────────────────────────────────────────────────
@Composable
internal fun ExtraLargePlayerLayout(
    state: PlayerState,
    onEvent: (PlayerEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        // Player pane (0.4)
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            NowPlayingArtwork(
                artworkUrl = state.currentEpisode?.thumbnail,
                isPlaying = state.isPlaying,
                size = 300.dp,
            )
            Spacer(Modifier.height(24.dp))
            NowPlayingInfo(
                episode = state.currentEpisode,
                modifier = Modifier.fillMaxWidth(),
                titleStyle = MaterialTheme.typography.headlineSmall,
            )
            Spacer(Modifier.height(20.dp))
            PlayerScrubber(
                positionMs = state.positionMs,
                durationMs = state.durationMs,
                onSeekTo = { onEvent(PlayerEvent.OnSeekTo(it)) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            PlayerControls(
                isPlaying = state.isPlaying,
                isLoading = state.isLoading,
                hasNext = state.hasNext,
                hasPrevious = state.hasPrevious,
                onEvent = onEvent
            )
            Spacer(Modifier.height(12.dp))
            SecondaryControls(
                playbackSpeed = state.playbackSpeed,
                isShuffleEnabled = state.isShuffleEnabled,
                repeatMode = state.repeatMode,
                onSetSpeed = { onEvent(PlayerEvent.OnSetSpeed) },
                onToggleShuffle = { onEvent(PlayerEvent.OnToggleShuffle) },
                onCycleRepeatMode = { onEvent(PlayerEvent.OnCycleRepeatMode) },
            )
        }

        // Episode info pane (0.25) — shown only on ExtraLarge
        EpisodeInfoPanel(
            episode = state.currentEpisode,
            modifier = Modifier
                .weight(0.25f)
                .fillMaxHeight(),
        )

        // Queue pane (0.35)
        QueuePanel(
            state = state,
            onEvent = onEvent,
            modifier = Modifier
                .weight(0.35f)
                .fillMaxHeight(),
        )
    }
}