package com.mak.pocketnotes.android.feature.player.v2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.PlayerState
import com.mak.pocketnotes.media.PlayerController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class PlayerViewModel(
    private val controller: PlayerController
): ViewModel() {
    val playerState: StateFlow<PlayerState> = controller.playerState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PlayerState(),
        )

    fun onEvent(event: PlayerEvent) {
        when(event) {
            PlayerEvent.OnCycleRepeatMode -> controller.cycleRepeatMode()
            is PlayerEvent.OnMoveQueueItem -> controller.moveQueueItem(event.fromIndex, event.toIndex)
            is PlayerEvent.OnRemoveFromQueue -> controller.removeFromQueue(event.index)
            is PlayerEvent.OnSeekTo -> controller.seekTo(event.duration)
            is PlayerEvent.OnSetSpeed -> controller.setPlaybackSpeed(event.speed)
            PlayerEvent.OnSkipBackward -> controller.skipForward()
            PlayerEvent.OnSkipForward -> controller.skipForward()
            PlayerEvent.OnSkipToNext -> controller.skipToNext()
            PlayerEvent.OnSkipToPrevious -> controller.skipToPrevious()
            is PlayerEvent.OnSkipToQueueItem -> controller.skipToQueueItem(event.queueIndex)
            PlayerEvent.OnTogglePlayPause -> controller.togglePlayPause()
            PlayerEvent.OnToggleShuffle -> controller.toggleShuffle()
        }
    }
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
    data class OnSetSpeed(val speed: Float) : PlayerEvent
    data object OnToggleShuffle : PlayerEvent
    data object OnCycleRepeatMode : PlayerEvent
    data class OnSkipToQueueItem(val queueIndex: Int) : PlayerEvent
    data class OnRemoveFromQueue(val index: Int) : PlayerEvent
    data class OnMoveQueueItem(val fromIndex: Int, val toIndex: Int) : PlayerEvent
}