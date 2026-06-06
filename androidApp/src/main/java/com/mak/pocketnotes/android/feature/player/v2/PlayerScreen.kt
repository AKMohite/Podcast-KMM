package com.mak.pocketnotes.android.feature.player.v2

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forward30
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.common.PodcastPlayer
import com.mak.pocketnotes.android.common.navigation.Navigator
import com.mak.pocketnotes.android.common.viewmodel.MediaViewModel
import com.mak.pocketnotes.android.ui.theme.adaptiveScreenInfo
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isExtraLarge
import com.mak.pocketnotes.android.ui.theme.isLarge
import com.mak.pocketnotes.android.ui.theme.isMedium
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.utils.sample.sampleEpisodes
import com.mak.pocketnotes.domain.models.PLAYBACK_SPEEDS
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.domain.models.RepeatMode
import org.koin.androidx.compose.koinViewModel

fun EntryProviderScope<NavKey>.nowPlayingEntry(
    mediaViewModel: MediaViewModel,
    navigator: Navigator
) {
    entry<PodcastPlayer> {
        PlayerScreen()
    }
}

internal object PlayerTestTags {
    const val PLAY_PAUSE_BUTTON = "player:play_pause"
    const val SKIP_NEXT_BUTTON = "player:skip_next"
    const val SKIP_PREV_BUTTON = "player:skip_prev"
    const val FORWARD_BUTTON = "player:forward"
    const val REWIND_BUTTON = "player:rewind"
    const val EPISODE_TITLE = "player:episode_title"
    const val PODCAST_NAME = "player:podcast_name"
    const val ARTWORK = "player:artwork"
    const val SCRUBBER = "player:scrubber"
    const val SPEED_BUTTON = "player:speed"
    const val SHUFFLE_BUTTON = "player:shuffle"
    const val REPEAT_BUTTON = "player:repeat"
    const val QUEUE_ITEM = "queue:item"
    const val QUEUE_ITEM_TITLE = "queue:item_title"
    const val MINI_PLAYER = "mini_player:container"
}

@Composable
internal fun PlayerScreen(
) {
    val viewModel: PlayerViewModel = koinViewModel()
    val state by viewModel.playerState.collectAsStateWithLifecycle()
    PlayerContent(
        modifier = Modifier
            .fillMaxSize(),
        state = state
    )
}

@Composable
private fun PlayerContent(
    modifier: Modifier = Modifier,
    state: PlayerState
) {
    val sizeClass = adaptiveScreenInfo().windowSizeClass
    when {
        sizeClass.isExtraLarge() -> ExtraLargePlayerLayout(
            state = state,
            onEvent = {}
        )
        sizeClass.isLarge() -> LargePlayerLayout(
            state = state,
            onEvent = {}
        )
        sizeClass.isExpanded() -> ExpandedPlayerLayout(
            state = state,
            onEvent = {},
        )
        sizeClass.isMedium() -> MediumPlayerLayout(
            state = state,
            onEvent = {},
            onShowQueue = {}
        )
        else -> CompactPlayer(
            state = state,
            onEvent = {},
            onShowQueue = {}
        )
    }
}

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
private fun CompactPlayer(
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
                onEvent(PlayerEvent.OnSetSpeed)
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
            Text("Queue (${state.queue.size})")
        }
    }
}


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


@Composable
private fun EpisodeInfoPanel(
    episode: PodcastEpisode?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(16.dp),
            )
            .padding(16.dp),
    ) {
        Text(
            text = "About this episode",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = episode?.description ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            overflow = TextOverflow.Ellipsis,
        )

        // Chapters list (if available)
        /*if ((episode?.chapters?.size ?: 0) > 0) {
            Spacer(Modifier.height(16.dp))
            Text(text = "Chapters", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            LazyColumn {
                itemsIndexed(episode!!.chapters) { _, chapter ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = formatDuration(chapter.startMs),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.width(52.dp),
                        )
                        Text(
                            text = chapter.title,
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                }
            }
        }*/
    }
}


@Composable
private fun QueuePanel(
    state: PlayerState,
    onEvent: (PlayerEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(16.dp),
            )
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Up Next",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "${state.upcomingEpisodes.size} episodes",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(8.dp))

        // Currently playing row
        state.currentEpisode?.let { ep ->
            QueueItemRow(
                episode = ep,
                index = state.currentQueueIndex,
                isCurrentlyPlaying = true,
                onPlay = { onEvent(PlayerEvent.OnSkipToQueueItem(state.currentQueueIndex)) },
                onRemove = {},  // can't remove the currently playing item
            )
            Divider(modifier = Modifier.padding(vertical = 4.dp))
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(
                items = state.upcomingEpisodes,
                key = { _, indexed -> indexed.episode.id },
            ) { _, indexed ->
                QueueItemRow(
                    episode = indexed.episode,
                    index = indexed.index,
                    isCurrentlyPlaying = false,
                    onPlay = { onEvent(PlayerEvent.OnSkipToQueueItem(indexed.index)) },
                    onRemove = { onEvent(PlayerEvent.OnRemoveFromQueue(indexed.index)) },
                )
            }
        }
    }
}


@Composable
private fun NowPlayingArtwork(
    artworkUrl: String?,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 280.dp,
) {
    val scale by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0.92f,
        animationSpec = tween(durationMillis = 300),
        label = "artwork_scale",
    )

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(16.dp))
            .testTag(PlayerTestTags.ARTWORK),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = artworkUrl,
            contentDescription = "Podcast artwork",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
    }
}


@Composable
private fun NowPlayingInfo(
    episode: PodcastEpisode?,
    modifier: Modifier = Modifier,
    titleStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleLarge,
) {
    Column(modifier = modifier) {
        Text(
            text = episode?.title ?: "Nothing playing",
            style = titleStyle,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.testTag(PlayerTestTags.EPISODE_TITLE),
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = episode?.title.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.testTag(PlayerTestTags.PODCAST_NAME),
        )
    }
}

@Composable
private fun PlayerScrubber(
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

@Composable
private fun PlayerControls(
    isPlaying: Boolean,
    isLoading: Boolean,
    hasNext: Boolean,
    hasPrevious: Boolean,
    onEvent: (PlayerEvent) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
) {
    val iconSize = if (isCompact) 20.dp else 24.dp
    val playSize = if (isCompact) 48.dp else 64.dp

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Rewind 10 s
        IconButton(
            onClick = {
                onEvent(PlayerEvent.OnSkipBackward)
            },
            modifier = Modifier.testTag(PlayerTestTags.REWIND_BUTTON),
        ) {
            Icon(
                Icons.Default.Replay10,
                contentDescription = "Skip back 10 seconds",
                modifier = Modifier.size(iconSize + 4.dp),
            )
        }

        // Skip previous
        IconButton(
            onClick = {
                onEvent(PlayerEvent.OnSkipToPrevious)
            },
            enabled = hasPrevious,
            modifier = Modifier.testTag(PlayerTestTags.SKIP_PREV_BUTTON),
        ) {
            Icon(
                Icons.Default.SkipPrevious,
                contentDescription = "Previous episode",
                modifier = Modifier.size(iconSize + 4.dp),
            )
        }

        // Play / Pause (large)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(playSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        onEvent(PlayerEvent.OnTogglePlayPause)
                    },
                )
                .testTag(PlayerTestTags.PLAY_PAUSE_BUTTON)
                .semantics {
                    contentDescription = if (isPlaying) "Pause" else "Play"
                },
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(iconSize + 4.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(iconSize + 8.dp),
                )
            }
        }

        // Skip next
        IconButton(
            onClick = {
                onEvent(PlayerEvent.OnSkipToNext)
            },
            enabled = hasNext,
            modifier = Modifier.testTag(PlayerTestTags.SKIP_NEXT_BUTTON),
        ) {
            Icon(
                Icons.Default.SkipNext,
                contentDescription = "Next episode",
                modifier = Modifier.size(iconSize + 4.dp),
            )
        }

        // Forward 30 s
        IconButton(
            onClick = {
                onEvent(PlayerEvent.OnSkipForward)
            },
            modifier = Modifier.testTag(PlayerTestTags.FORWARD_BUTTON),
        ) {
            Icon(
                Icons.Default.Forward30,
                contentDescription = "Skip forward 30 seconds",
                modifier = Modifier.size(iconSize + 4.dp),
            )
        }
    }
}

@Composable
private fun SecondaryControls(
    playbackSpeed: Float,
    isShuffleEnabled: Boolean,
    repeatMode: RepeatMode,
    onSetSpeed: (Float) -> Unit,
    onToggleShuffle: () -> Unit,
    onCycleRepeatMode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Speed button cycles through PLAYBACK_SPEEDS
        SpeedChip(speed = playbackSpeed, onSetSpeed = onSetSpeed)

        // Shuffle
        IconButton(
            onClick = onToggleShuffle,
            modifier = Modifier.testTag(PlayerTestTags.SHUFFLE_BUTTON),
        ) {
            Icon(
                Icons.Default.Shuffle,
                contentDescription = if (isShuffleEnabled) "Shuffle on" else "Shuffle off",
                tint = if (isShuffleEnabled) MaterialTheme.colorScheme.primary
                else LocalContentColor.current.copy(alpha = 0.5f),
            )
        }

        // Repeat
        IconButton(
            onClick = onCycleRepeatMode,
            modifier = Modifier.testTag(PlayerTestTags.REPEAT_BUTTON),
        ) {
            Icon(
                imageVector = when (repeatMode) {
                    RepeatMode.ONE -> Icons.Default.RepeatOne
                    else -> Icons.Default.Repeat
                },
                contentDescription = "Repeat mode: $repeatMode",
                tint = if (repeatMode != RepeatMode.NONE) MaterialTheme.colorScheme.primary
                else LocalContentColor.current.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
private fun SpeedChip(speed: Float, onSetSpeed: (Float) -> Unit) {
    val currentIndex = PLAYBACK_SPEEDS.indexOf(speed).takeIf { it >= 0 } ?: 2
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                val nextIndex = (currentIndex + 1) % PLAYBACK_SPEEDS.size
                onSetSpeed(PLAYBACK_SPEEDS[nextIndex])
            }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(PlayerTestTags.SPEED_BUTTON),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "${speed}x",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun QueueItemRow(
    episode: PodcastEpisode,
    index: Int,
    isCurrentlyPlaying: Boolean,
    onPlay: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onPlay)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("${PlayerTestTags.QUEUE_ITEM}:$index"),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Artwork thumbnail
        AsyncImage(
            model = episode.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = episode.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isCurrentlyPlaying) FontWeight.Bold else FontWeight.Normal,
                color = if (isCurrentlyPlaying) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.testTag("${PlayerTestTags.QUEUE_ITEM_TITLE}:$index"),
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = formatDuration(episode.duration * 1_000L),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        // Remove button
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove ${episode.title} from queue",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MiniPlayer(
    state: PlayerState,
    onExpand: () -> Unit,
    onTogglePlayPause: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.currentEpisode == null) return

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .testTag(PlayerTestTags.MINI_PLAYER),
        shadowElevation = 8.dp,
        tonalElevation = 4.dp,
        onClick = onExpand,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Thumbnail
            AsyncImage(
                model = state.currentEpisode.thumbnail,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(6.dp)),
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.currentEpisode.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = state.currentEpisode.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // Mini play/pause
            IconButton(onClick = onTogglePlayPause) {
                Icon(
                    imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (state.isPlaying) "Pause" else "Play",
                )
            }

            // Progress indicator
            LinearProgressIndicator(
                progress = { state.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.Bottom),
            )
        }
    }
}

private fun formatDuration(ms: Long): String {
    if (ms <= 0L) return "0:00"
    val totalSeconds = ms / 1_000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%d:%02d".format(minutes, seconds)
    }
}

@Preview
@PreviewScreenSizes
@Composable
private fun PlayerContentPreview() {
    PocketNotesTheme {
        Surface {
            PlayerContent(
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
                )
            )
        }
    }
}