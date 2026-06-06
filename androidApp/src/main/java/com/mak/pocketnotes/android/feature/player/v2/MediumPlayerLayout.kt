package com.mak.pocketnotes.android.feature.player.v2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.feature.player.v2.components.NowPlayingArtwork
import com.mak.pocketnotes.android.feature.player.v2.components.NowPlayingInfo
import com.mak.pocketnotes.android.feature.player.v2.components.PlayerControls
import com.mak.pocketnotes.android.feature.player.v2.components.PlayerScrubber
import com.mak.pocketnotes.android.feature.player.v2.components.SecondaryControls

// ─────────────────────────────────────────────────────────────────────────────
// MEDIUM  600–839 dp  ·  Phone landscape / small tablet
//
//  ┌──────────────┬────────────────────────┐
//  │  [Artwork]   │  Title                 │
//  │  200dp       │  Podcast               │
//  │              │                        │
//  │              │  ─── scrubber ───      │
//  │              │  [⏮][⏸/▶][⏭]        │
//  │              │  speed  shuffle  rep   │
//  └──────────────┴────────────────────────┘
// ─────────────────────────────────────────────────────────────────────────────
@Composable
internal fun MediumPlayerLayout(
    state: PlayerState,
    onEvent: (PlayerEvent) -> Unit,
    onShowQueue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left: artwork
        NowPlayingArtwork(
            artworkUrl = state.currentEpisode?.thumbnail,
            isPlaying = state.isPlaying,
            size = 200.dp,
        )

        Spacer(Modifier.width(24.dp))

        // Right: controls stack
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            NowPlayingInfo(
                episode = state.currentEpisode,
                modifier = Modifier.fillMaxWidth(),
                titleStyle = MaterialTheme.typography.titleMedium,
            )
            PlayerScrubber(
                positionMs = state.positionMs,
                durationMs = state.durationMs,
                onSeekTo = { onEvent(PlayerEvent.OnSeekTo(it)) },
                modifier = Modifier.fillMaxWidth(),
            )
            PlayerControls(
                isPlaying = state.isPlaying,
                isLoading = state.isLoading,
                hasNext = state.hasNext,
                hasPrevious = state.hasPrevious,
                onEvent = onEvent,
                isCompact = true,
            )
            SecondaryControls(
                playbackSpeed = state.playbackSpeed,
                isShuffleEnabled = state.isShuffleEnabled,
                repeatMode = state.repeatMode,
                onSetSpeed = { onEvent(PlayerEvent.OnSetSpeed) },
                onToggleShuffle = { onEvent(PlayerEvent.OnToggleShuffle) },
                onCycleRepeatMode = { onEvent(PlayerEvent.OnCycleRepeatMode) },
            )
            TextButton(onClick = onShowQueue) {
                Icon(Icons.Default.QueueMusic, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("View queue (${state.queue.size})")
            }
        }
    }
}