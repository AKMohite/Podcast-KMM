package com.mak.pocketnotes.android.feature.player.v2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.mak.pocketnotes.android.feature.player.v2.components.SecondaryControls
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.PlayerState
import com.mak.pocketnotes.domain.models.RepeatMode
import com.mak.pocketnotes.utils.sample.sampleEpisodes

/**
 * Tabletop posture layout for foldables (horizontal hinge).
 * Top half: Artwork
 * Bottom half: Controls
 */
@Composable
internal fun TabletopPlayerLayout(
  state: PlayerState,
  onEvent: (PlayerEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxSize()
  ) {
    // Top half: Artwork
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      NowPlayingArtwork(
        artworkUrl = state.currentEpisode?.thumbnail,
        isPlaying = state.isPlaying,
        size = 280.dp
      )
    }

    // Bottom half: Controls
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      NowPlayingInfo(
        episode = state.currentEpisode,
        modifier = Modifier.fillMaxWidth(),
        titleStyle = MaterialTheme.typography.headlineSmall
      )
      Spacer(Modifier.height(16.dp))
      PlayerScrubber(
        positionMs = state.positionMs,
        durationMs = state.durationMs,
        onSeekTo = { onEvent(PlayerEvent.OnSeekTo(it)) },
        modifier = Modifier.fillMaxWidth()
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
        onSetSpeed = { onEvent(PlayerEvent.OnSetSpeed(it)) },
        onToggleShuffle = { onEvent(PlayerEvent.OnToggleShuffle) },
        onCycleRepeatMode = { onEvent(PlayerEvent.OnCycleRepeatMode) }
      )
    }
  }
}

@Preview(device = "spec:width=673dp,height=841dp,orientation=portrait")
@Composable
private fun TabletopPlayerLayoutPreview() {
  PocketNotesTheme {
    Surface {
      TabletopPlayerLayout(
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
