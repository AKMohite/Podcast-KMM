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

/**
 * Book posture layout for foldables (vertical hinge).
 * Left half: Artwork and info and Controls
 * Right half: queue
 */
@Composable
internal fun BookPlayerLayout(
  state: PlayerState,
  onEvent: (PlayerEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxSize()
  ) {

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      NowPlayingArtwork(
        artworkUrl = state.currentEpisode?.thumbnail,
        isPlaying = state.isPlaying,
        size = 240.dp
      )
      Spacer(Modifier.height(24.dp))
      NowPlayingInfo(
        episode = state.currentEpisode,
        modifier = Modifier.fillMaxWidth(),
        titleStyle = MaterialTheme.typography.titleLarge
      )
      Column(
        modifier = Modifier
          .weight(1f)
          .fillMaxHeight()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
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
          onEvent = onEvent,
          isCompact = true
        )
        SecondaryControls(
          playbackSpeed = state.playbackSpeed,
          isShuffleEnabled = state.isShuffleEnabled,
          repeatMode = state.repeatMode,
          onSetSpeed = { onEvent(PlayerEvent.OnSetSpeed(it)) },
          onToggleShuffle = { onEvent(PlayerEvent.OnToggleShuffle) },
          onCycleRepeatMode = { onEvent(PlayerEvent.OnCycleRepeatMode) }
        )
        Spacer(Modifier.height(8.dp))
      }
    }

    QueuePanel(
      state = state,
      onEvent = onEvent,
      modifier = Modifier
        .padding(top = 24.dp, bottom = 24.dp, end = 24.dp)
        .weight(1f)
    )
  }
}

@Preview(device = "spec:width=673dp,height=841dp,orientation=landscape")
@Composable
private fun BookPlayerLayoutPreview() {
  PocketNotesTheme {
    Surface {
      BookPlayerLayout(
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
