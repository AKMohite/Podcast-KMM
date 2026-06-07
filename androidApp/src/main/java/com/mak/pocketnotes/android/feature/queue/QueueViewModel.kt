package com.mak.pocketnotes.android.feature.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.android.feature.player.v2.PlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

internal class QueueViewModel: ViewModel() {

    val playerState: StateFlow<PlayerState> = MutableStateFlow(PlayerState())

}

internal sealed interface QueueEvent {
    object ClearQueue: QueueEvent
    data class RemoveItem(val index: Int): QueueEvent
    data class MoveItem(val from: Int, val to: Int): QueueEvent
    data class PlayItem(val index: Int): QueueEvent
}
