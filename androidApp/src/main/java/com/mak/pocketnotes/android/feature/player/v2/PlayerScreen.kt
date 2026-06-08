package com.mak.pocketnotes.android.feature.player.v2

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mak.pocketnotes.android.common.PlayerQueue
import com.mak.pocketnotes.android.common.PodcastPlayer
import com.mak.pocketnotes.android.common.navigation.Navigator
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.ui.theme.adaptiveScreenInfo
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isExtraLarge
import com.mak.pocketnotes.android.ui.theme.isLarge
import com.mak.pocketnotes.android.ui.theme.isMedium
import com.mak.pocketnotes.domain.models.PlayerState
import com.mak.pocketnotes.domain.models.RepeatMode
import com.mak.pocketnotes.utils.sample.sampleEpisodes
import org.koin.androidx.compose.koinViewModel

fun EntryProviderScope<NavKey>.nowPlayingEntry(
    navigator: Navigator
) {
    entry<PodcastPlayer> {
        PlayerScreen(
            onShowQueue = {
                navigator.navigate(PlayerQueue)
            }
        )
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
    onShowQueue: () -> Unit
) {
    val viewModel: PlayerViewModel = koinViewModel(
//        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )
    val state by viewModel.playerState.collectAsStateWithLifecycle()
    PlayerContent(
        modifier = Modifier
            .fillMaxSize(),
        state = state,
        onEvent = viewModel::onEvent,
        onShowQueue = onShowQueue
    )
}

@Composable
private fun PlayerContent(
    modifier: Modifier = Modifier,
    state: PlayerState,
    onEvent: (PlayerEvent) -> Unit,
    onShowQueue: () -> Unit
) {
    val sizeClass = adaptiveScreenInfo().windowSizeClass
    when {
        sizeClass.isExtraLarge() -> ExtraLargePlayerLayout(
            state = state,
            onEvent = onEvent,
            modifier = modifier
        )
        sizeClass.isLarge() -> LargePlayerLayout(
            state = state,
            onEvent = onEvent,
            modifier = modifier
        )
        sizeClass.isExpanded() -> ExpandedPlayerLayout(
            state = state,
            onEvent = onEvent,
            modifier = modifier
        )
        sizeClass.isMedium() -> MediumPlayerLayout(
            state = state,
            onEvent = onEvent,
            onShowQueue = onShowQueue,
            modifier = modifier
        )
        else -> CompactPlayer(
            state = state,
            onEvent = onEvent,
            onShowQueue = onShowQueue,
            modifier = modifier
        )
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
                ),
                onEvent = {},
                onShowQueue = {}
            )
        }
    }
}