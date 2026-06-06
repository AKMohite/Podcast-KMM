package com.mak.pocketnotes.android.feature.player.v2

import androidx.lifecycle.ViewModel
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.domain.models.RepeatMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class PlayerViewModel: ViewModel() {
    val playerState: StateFlow<PlayerState> = MutableStateFlow(PlayerState())

    fun onEvent(event: PlayerEvent) {}
}

internal sealed interface PlayerEvent {

    /*data class PlayerCallbacks(

    val onMoveQueueItem: (Int, Int) -> Unit = { _, _ -> },
)*/
    data object OnSkipBackward : PlayerEvent
    data object OnSkipToPrevious : PlayerEvent
    data object OnTogglePlayPause : PlayerEvent
    data object OnSkipToNext : PlayerEvent
    data object OnSkipForward : PlayerEvent
    data class OnSeekTo(val duration: Long) : PlayerEvent
    data object OnSetSpeed : PlayerEvent
    data object OnToggleShuffle : PlayerEvent
    data object OnCycleRepeatMode : PlayerEvent
    data class OnSkipToQueueItem(val queueIndex: Int) : PlayerEvent
    data class OnRemoveFromQueue(val index: Int) : PlayerEvent
    data class OnMoveQueueItem(val fromIndex: Int, val toIndex: Int) : PlayerEvent
}

internal sealed interface PlayerError {
    data class NetworkError(val message: String) : PlayerError
    data class PlaybackError(val message: String) : PlayerError
    data object AudioFocusLost : PlayerError
    data object Unknown : PlayerError
}

/** Carries the queue index alongside the episode so the UI can call skipToQueueItem. */
internal data class IndexedEpisode(val index: Int, val episode: PodcastEpisode)
internal data class PlayerState(
    val currentEpisode: PodcastEpisode? = null,
    val queue: List<PodcastEpisode> = emptyList(),
    val currentQueueIndex: Int = -1,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val error: PlayerError? = null,
    val isSleepTimerActive: Boolean = false,
    val sleepTimerRemainingMs: Long = 0L,
) {
    /** 0f..1f scrubber position */
    val progress: Float
        get() = if (durationMs > 0L) positionMs.toFloat() / durationMs.toFloat() else 0f

    val remainingMs: Long
        get() = (durationMs - positionMs).coerceAtLeast(0L)

    val hasNext: Boolean
        get() = when (repeatMode) {
            RepeatMode.ALL -> queue.isNotEmpty()
            else -> currentQueueIndex < queue.size - 1
        }

    val hasPrevious: Boolean
        get() = currentQueueIndex > 0

    /** Episodes after the current one (shown in queue panel). */
    val upcomingEpisodes: List<IndexedEpisode>
        get() = queue
            .drop((currentQueueIndex + 1).coerceAtLeast(0))
            .mapIndexed { offset, ep ->
                IndexedEpisode(index = currentQueueIndex + 1 + offset, episode = ep)
            }

    val isIdle: Boolean
        get() = currentEpisode == null
}