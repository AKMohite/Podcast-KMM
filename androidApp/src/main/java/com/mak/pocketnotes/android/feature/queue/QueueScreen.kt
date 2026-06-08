package com.mak.pocketnotes.android.feature.queue

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import coil.compose.AsyncImage
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.common.PlayerQueue
import com.mak.pocketnotes.android.common.navigation.Navigator
import com.mak.pocketnotes.android.feature.player.v2.components.formatDuration
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.PlayerState
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.utils.sample.sampleEpisodes
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.playQueueEntry(
    navigator: Navigator
) {
    entry<PlayerQueue> {
        QueueScreen()
    }
}

object QueueTestTags {
    const val SCREEN = "queue:screen"
    const val CLEAR_BUTTON = "queue:clear"
    const val EMPTY_STATE = "queue:empty"
    const val LIST = "queue:list"
    fun item(index: Int) = "queue:item:$index"
    fun itemTitle(index: Int) = "queue:item_title:$index"
    fun removeItem(index: Int) = "queue:remove:$index"
    fun playItem(index: Int) = "queue:play:$index"
}

@Composable
internal fun QueueScreen() {
    val viewModel: QueueViewModel = koinViewModel()
    val state by viewModel.playerState.collectAsStateWithLifecycle()
    QueueContent(
        state = PlayerState(
            queue = sampleEpisodes.take(5),
            currentQueueIndex = 0
        ),
        onEvent = {}
    )
}

@Composable
private fun QueueContent(
    state: PlayerState,
    onEvent: (QueueEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.up_next)) },
                actions = {
                    if (state.queue.isNotEmpty()) {
                        TextButton(
                            onClick = { onEvent(QueueEvent.ClearQueue) },
                            modifier = Modifier.testTag(QueueTestTags.CLEAR_BUTTON),
                        ) {
                            Text(stringResource(R.string.clear_all))
                        }
                    }
                },
            )
        },
        modifier = Modifier.testTag(QueueTestTags.SCREEN),
    ) { padding ->
        QueueContent(
            state = state,
            onPlayItem = { onEvent(QueueEvent.PlayItem(it)) },
            onRemoveItem = { onEvent(QueueEvent.RemoveItem(it)) },
            onMoveItem = { from, to ->
                onEvent(QueueEvent.MoveItem(from, to))
            },
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun QueueContent(
    state: PlayerState,
    onPlayItem: (Int) -> Unit,
    onRemoveItem: (Int) -> Unit,
    onMoveItem: (from: Int, to: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.queue.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .testTag(QueueTestTags.EMPTY_STATE),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.empty_player_queue),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.add_episodes_to_queue),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        return
    }

    // Drag state — tracks which item is being dragged and how far
    var draggingIndex by remember { mutableIntStateOf(-1) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag(QueueTestTags.LIST),
    ) {
        itemsIndexed(
            items = state.queue,
            key = { _, ep -> ep.id },
        ) { index, episode ->
            val isCurrentlyPlaying = index == state.currentQueueIndex

            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { dismissValue ->
                    if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                        onRemoveItem(index)
                        true
                    } else false
                },
            )

            SwipeToDismissBox(
                state = dismissState,
//                directions = setOf(DismissDirection.EndToStart),
                backgroundContent = {
                    SwipeDismissBackground()
                },
                modifier = Modifier.animateContentSize(),
            ) {
                DraggableQueueItem(
                    episode = episode,
                    index = index,
                    isCurrentlyPlaying = isCurrentlyPlaying,
                    isDragging = draggingIndex == index,
                    onPlay = { onPlayItem(index) },
                    onRemove = { onRemoveItem(index) },
                    onDragStart = { draggingIndex = index },
                    onDrag = { dy -> dragOffsetY += dy },
                    onDragEnd = {
                        if (draggingIndex >= 0) {
                            val itemHeightPx = 72f
                            val targetIndex = (draggingIndex + (dragOffsetY / itemHeightPx).toInt())
                                .coerceIn(0, state.queue.lastIndex)
                            if (targetIndex != draggingIndex) {
                                onMoveItem(draggingIndex, targetIndex)
                            }
                        }
                        draggingIndex = -1
                        dragOffsetY = 0f
                    },
                )
            }
        }
    }
}

@Composable
private fun DraggableQueueItem(
    episode: PodcastEpisode,
    index: Int,
    isCurrentlyPlaying: Boolean,
    isDragging: Boolean,
    onPlay: () -> Unit,
    onRemove: () -> Unit,
    onDragStart: () -> Unit,
    onDrag: (dy: Float) -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bgColor = if (isCurrentlyPlaying)
        MaterialTheme.colorScheme.primaryContainer
    else if (isDragging)
        MaterialTheme.colorScheme.surfaceVariant
    else
        MaterialTheme.colorScheme.surface

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag(QueueTestTags.item(index)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Drag handle — long-press activates drag
        Icon(
            imageVector = Icons.Default.DragHandle,
            contentDescription = stringResource(R.string.drag_to_reorder),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(20.dp)
                .pointerInput(index) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { onDragStart() },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            onDrag(dragAmount.y)
                        },
                        onDragEnd = { onDragEnd() },
                        onDragCancel = { onDragEnd() },
                    )
                },
        )

        Spacer(Modifier.width(8.dp))

        // Artwork
        AsyncImage(
            model = episode.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )

        Spacer(Modifier.width(12.dp))

        // Title + duration
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = episode.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isCurrentlyPlaying) FontWeight.Bold else FontWeight.Normal,
                color = if (isCurrentlyPlaying)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.testTag(QueueTestTags.itemTitle(index)),
            )
            Text(
                text = "${episode.title} · ${formatDuration(episode.duration * 1_000L)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        // Play button
        FilledTonalIconButton(
            onClick = onPlay,
            modifier = Modifier
                .size(36.dp)
                .testTag(QueueTestTags.playItem(index)),
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = stringResource(R.string.play_episode, episode.title),
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun SwipeDismissBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.remove_queue_episode),
            tint = MaterialTheme.colorScheme.onErrorContainer,
        )
    }
}

@Preview(showBackground = true)
@PreviewScreenSizes
@Composable
private fun QueueContentPreview() {
    PocketNotesTheme {
        QueueContent(
            state = PlayerState(
                queue = sampleEpisodes.take(5),
                currentQueueIndex = 0
            ),
            onEvent = {}
        )
    }
}

