package app.mak.pocketnotes.wearos.feature.nowplaying

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Forward30
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.CompactButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TimeText
import app.mak.pocketnotes.wearos.R
import app.mak.pocketnotes.wearos.presentation.theme.WearPocketNotesTheme
import coil.compose.AsyncImage

object NowPlayingTestTags {
    const val SCREEN         = "now_playing"
    const val ARTWORK        = "now_playing:artwork"
    const val EPISODE_TITLE  = "now_playing:episode_title"
    const val PODCAST_NAME   = "now_playing:podcast_name"
    const val PLAY_PAUSE     = "now_playing:play_pause"
    const val SKIP_NEXT      = "now_playing:skip_next"
    const val SKIP_PREV      = "now_playing:skip_prev"
    const val SKIP_FWD       = "now_playing:skip_fwd"
    const val SKIP_BWD       = "now_playing:skip_bwd"
    const val SPEED_CHIP     = "now_playing:speed"
    const val PROGRESS       = "now_playing:progress"
    const val IDLE_HINT      = "now_playing:idle_hint"
}

@Composable
internal fun NowPlayingScreen(
    onNavigateToQueue: () -> Unit
) {
//    TODO()
    val viewModel: WearPlayerViewmodel = WearPlayerViewmodel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NowPlayingContent(
        state = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToQueue = onNavigateToQueue
    )
}

@Composable
fun NowPlayingContent(
    state: WearUiState,
    onEvent: (WearPlayEvent) -> Unit,
    onNavigateToQueue: () -> Unit
) {
    ScreenScaffold(
        timeText = { TimeText() },
//        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
//        positionIndicator = {
//            PositionIndicator(scalingLazyListState = rememberScalingLazyListState())
//        },
        modifier = Modifier.testTag(NowPlayingTestTags.SCREEN),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            if (state.player.isIdle) {
                IdleContent(
                    modifier = Modifier.testTag(NowPlayingTestTags.IDLE_HINT)
                )
            } else {
                PlayerContent(
                    player = state.player,
                    onEvent = onEvent,
                    onNavigateToQueue = onNavigateToQueue
                )
            }
        }
    }
}

@Composable
private fun PlayerContent(
    player: WearPlayerState,
    onEvent: (WearPlayEvent) -> Unit,
    onNavigateToQueue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.height(20.dp))
        AsyncImage(
            model = player.artworkUrl.ifEmpty { null },
            contentDescription = "Artwork",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .testTag(NowPlayingTestTags.ARTWORK),
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = player.episodeTitle,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(NowPlayingTestTags.EPISODE_TITLE),
        )

        Text(
            text = player.podcastTitle,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(NowPlayingTestTags.PODCAST_NAME),
        )

        Spacer(Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .testTag(NowPlayingTestTags.PROGRESS),
        ) {
            CircularProgressIndicator(
                progress         = { player.progress },
                modifier         = Modifier.fillMaxSize(),
                strokeWidth      = 3.dp,
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompactButton(
                onClick = { onEvent(WearPlayEvent.OnSkipPrevious) },
                enabled = player.hasPrevious,
                modifier = Modifier.testTag(NowPlayingTestTags.SKIP_PREV),
            ) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous")
            }

            // Central play/pause — larger
            Button(
                onClick  = { onEvent(WearPlayEvent.OnTogglePlay) },
                modifier = Modifier
                    .size(48.dp)
                    .testTag(NowPlayingTestTags.PLAY_PAUSE),
            ) {
                if (player.isLoading) {
                    CircularProgressIndicator(
                        modifier       = Modifier.size(20.dp),
                        strokeWidth    = 2.dp,
                    )
                } else {
                    Icon(
                        imageVector     = if (player.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (player.isPlaying) "Pause" else "Play",
                        modifier        = Modifier.size(24.dp),
                    )
                }
            }

            CompactButton(
                onClick  = { onEvent(WearPlayEvent.OnSkipNext) },
                enabled  = player.hasNext,
                modifier = Modifier.testTag(NowPlayingTestTags.SKIP_NEXT),
            ) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next")
            }
        }

        Spacer(Modifier.height(4.dp))

        // ── Secondary controls: rewind · speed · forward · queue ──
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompactButton(
                onClick  = { onEvent(WearPlayEvent.OnSkipBackward) },
                modifier = Modifier.testTag(NowPlayingTestTags.SKIP_BWD),
            ) {
                Icon(Icons.Default.Replay10, contentDescription = "Rewind 10s", Modifier.size(16.dp))
            }

            CompactButton(
                onClick  = { onEvent(WearPlayEvent.OnCycleSpeed) },
                modifier = Modifier.testTag(NowPlayingTestTags.SPEED_CHIP),
            ) {
                Text(
                    text  = "${player.playbackSpeed}x",
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            CompactButton(
                onClick  = { onEvent(WearPlayEvent.OnSkipForward) },
                modifier = Modifier.testTag(NowPlayingTestTags.SKIP_FWD),
            ) {
                Icon(Icons.Default.Forward30, contentDescription = "Forward 30s", Modifier.size(16.dp))
            }

            CompactButton(
                onClick = onNavigateToQueue,
            ) {
                Icon(Icons.AutoMirrored.Filled.QueueMusic, contentDescription = "Queue", Modifier.size(16.dp))
            }
        }
    }
}


@Composable
private fun IdleContent(modifier: Modifier = Modifier) {
    Column(
        modifier            = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector        = Icons.Default.PlayArrow,
            contentDescription = null,
            modifier           = Modifier.size(32.dp),
            tint               = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text      = stringResource(R.string.empty_now_player),
            style     = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            color     = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
    }
}

@Preview(
    name = "Round Watch",
    device = "id:wearos_small_round",
    showSystemUi = true,
    backgroundColor = 0xFF000000,
    showBackground = true
)
@Composable
private fun PlayerContentPreview() {
    WearPocketNotesTheme {
        PlayerContent(
            player = WearPlayerState(),
            onEvent = {},
            onNavigateToQueue = {}
        )
    }
}