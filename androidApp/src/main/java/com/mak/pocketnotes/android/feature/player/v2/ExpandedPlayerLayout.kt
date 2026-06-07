package com.mak.pocketnotes.android.feature.player.v2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.mak.pocketnotes.domain.models.PlayerState
import com.mak.pocketnotes.domain.models.RepeatMode
import com.mak.pocketnotes.utils.sample.sampleEpisodes


// ─────────────────────────────────────────────────────────────────────────────
// EXPANDED  840–1199 dp  ·  Tablet portrait/landscape
//
//  ┌───────────────────────┬──────────────────────┐
//  │  [Artwork 240dp]      │  Queue               │
//  │  Title                │  ─────────────────   │
//  │  Podcast              │  ▶ Episode 1 [now]   │
//  │                       │    Episode 2         │
//  │  ─── scrubber ───    │    Episode 3         │
//  │  [⏮][⏸/▶][⏭]      │    Episode 4         │
//  │  speed shuffle rep    │                      │
//  └───────────────────────┴──────────────────────┘
// ─────────────────────────────────────────────────────────────────────────────
@Composable
internal fun ExpandedPlayerLayout(
    state: PlayerState,
    onEvent: (PlayerEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left pane: player
        Column(
            modifier = Modifier
                .weight(0.55f)
                .padding(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            NowPlayingArtwork(
                artworkUrl = state.currentEpisode?.thumbnail,
                isPlaying = state.isPlaying,
                size = 240.dp,
            )
            NowPlayingInfo(episode = state.currentEpisode, modifier = Modifier.fillMaxWidth())
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
                onEvent = onEvent
            )
            SecondaryControls(
                playbackSpeed = state.playbackSpeed,
                isShuffleEnabled = state.isShuffleEnabled,
                repeatMode = state.repeatMode,
                onSetSpeed = { onEvent(PlayerEvent.OnSetSpeed) },
                onToggleShuffle = { onEvent(PlayerEvent.OnToggleShuffle) },
                onCycleRepeatMode = { onEvent(PlayerEvent.OnCycleRepeatMode) },
            )
        }

        // Right pane: inline queue
        QueuePanel(
            state = state,
            onEvent = onEvent,
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight(),
        )
    }
}

@Preview(widthDp = 1000)
@Composable
private fun ExpandedPlayerLayoutPreview() {
    PocketNotesTheme {
        Surface {
            ExpandedPlayerLayout(
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