package com.mak.pocketnotes.android.feature.player.v2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.player.v2.components.NowPlayingArtwork
import com.mak.pocketnotes.android.feature.player.v2.components.NowPlayingInfo
import com.mak.pocketnotes.android.feature.player.v2.components.PlayerControls
import com.mak.pocketnotes.android.feature.player.v2.components.PlayerScrubber
import com.mak.pocketnotes.android.feature.player.v2.components.SecondaryControls
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.PlayerState
import com.mak.pocketnotes.domain.models.RepeatMode
import com.mak.pocketnotes.utils.sample.sampleEpisodes

// ─────────────────────────────────────────────────────────────────────────────
// COMPACT  < 600 dp  ·  Phone portrait
//
//  ┌────────────────────┐
//  │  [Artwork 280dp]   │
//  │  Title             │
//  │  Podcast           │
//  │  ─── scrubber ─── │
//  │  [⏮][⏸/▶][⏭]   │
//  │  speed shuffle rep │
//  │  [Queue →]         │
//  └────────────────────┘
// ─────────────────────────────────────────────────────────────────────────────
@Composable
internal fun CompactPlayer(
    state: PlayerState,
    onEvent: (PlayerEvent) -> Unit,
    onShowQueue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        NowPlayingArtwork(
            artworkUrl = state.currentEpisode?.thumbnail,
            isPlaying = state.isPlaying,
            size = 260.dp,
        )

        Spacer(Modifier.height(8.dp))

        // Title + podcast
        NowPlayingInfo(
            episode = state.currentEpisode,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(8.dp))

        // Scrubber
        PlayerScrubber(
            positionMs = state.positionMs,
            durationMs = state.durationMs,
            onSeekTo = {
                onEvent(PlayerEvent.OnSeekTo(it))
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(4.dp))

        // Transport controls
        PlayerControls(
            isPlaying = state.isPlaying,
            isLoading = state.isLoading,
            hasNext = state.hasNext,
            hasPrevious = state.hasPrevious,
            onEvent = onEvent,
        )

        // Secondary controls
        SecondaryControls(
            playbackSpeed = state.playbackSpeed,
            isShuffleEnabled = state.isShuffleEnabled,
            repeatMode = state.repeatMode,
            onSetSpeed = {
                onEvent(PlayerEvent.OnSetSpeed(it))
            },
            onToggleShuffle = {
                onEvent(PlayerEvent.OnToggleShuffle)
            },
            onCycleRepeatMode = {
                onEvent(PlayerEvent.OnCycleRepeatMode)
            },
        )

        // Queue button
        OutlinedButton(
            onClick = onShowQueue,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.AutoMirrored.Filled.QueueMusic, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.queue_size, state.queue.size))
        }
    }
}

@Preview
@Composable
private fun CompactPlayerPreview() {
    PocketNotesTheme {
        Surface {
            CompactPlayer(
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
                onEvent = {},
                onShowQueue = {}
            )
        }
    }
}