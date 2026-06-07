package com.mak.pocketnotes.android.feature.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.PlayerState
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.media.PlayerController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class QueueViewModel(
    private val controller: PlayerController
): ViewModel() {

    val playerState: StateFlow<PlayerState> = controller.playerState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PlayerState(),
        )

    fun onEvent(event: QueueEvent) {
        when(event) {
            QueueEvent.ClearQueue -> controller.clearQueue()
            is QueueEvent.MoveItem -> controller.moveQueueItem(event.from, event.to)
            is QueueEvent.PlayItem -> controller.skipToQueueItem(event.index)
            is QueueEvent.RemoveItem -> controller.removeFromQueue(event.index)
            is QueueEvent.AddNext -> controller.addToQueueNext(event.episodes)
        }
    }

}

internal sealed interface QueueEvent {
    object ClearQueue: QueueEvent
    data class RemoveItem(val index: Int): QueueEvent
    data class AddNext(val episodes: PodcastEpisode): QueueEvent
    data class MoveItem(val from: Int, val to: Int): QueueEvent
    data class PlayItem(val index: Int): QueueEvent
}
