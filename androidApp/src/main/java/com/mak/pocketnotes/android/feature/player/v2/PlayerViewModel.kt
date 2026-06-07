package com.mak.pocketnotes.android.feature.player.v2

import androidx.lifecycle.ViewModel
import com.mak.pocketnotes.domain.models.PlayerState
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