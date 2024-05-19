package com.mak.pocketnotes.android.common.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.mak.pocketnotes.domain.models.PlayableEpisode
import com.mak.pocketnotes.service.media.service.IServiceHandler
import com.mak.pocketnotes.service.media.service.MediaEvent
import com.mak.pocketnotes.service.media.service.MediaState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MediaViewModel(
    private val serviceHandler: IServiceHandler,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private var duration by savedStateHandle.saveable { mutableStateOf(0L) }
    internal var progress by savedStateHandle.saveable { mutableStateOf(0f) }
    private var progressString by savedStateHandle.saveable { mutableStateOf("00:00") }
    internal var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }
    internal var currentSelectedMedia by savedStateHandle.saveable { mutableStateOf(PlayableEpisode.EMPTY) }
    private var mediaList by savedStateHandle.saveable { mutableStateOf(listOf<PlayableEpisode>()) }
    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val uiState = _uiState.asStateFlow()

    init {
        mediaHandlerEvents()
    }

    fun loadMedia(playableEpisodes: List<PlayableEpisode>) {
        viewModelScope.launch {
            mediaList = playableEpisodes
            serviceHandler.addMediaItems(playableEpisodes)
        }
    }

    fun onUIEvents(event: UIEvent) {
        viewModelScope.launch {
            when (event) {
                UIEvent.Backward -> serviceHandler.onPlayerEvents(MediaEvent.Backward)
                UIEvent.Forward -> serviceHandler.onPlayerEvents(MediaEvent.Forward)
                UIEvent.PlayPause -> {
                    serviceHandler.onPlayerEvents(MediaEvent.PLayPause)
                }
                is UIEvent.SeekTo -> serviceHandler.onPlayerEvents(MediaEvent.SeekTo, seekPosition = ((duration * event.position) / 100f).toLong())
                UIEvent.SeekToNext -> serviceHandler.onPlayerEvents(MediaEvent.SeekToNext)
                is UIEvent.SelectedAudioChange -> serviceHandler.onPlayerEvents(MediaEvent.SelectedAudioChange, selectedAudioIndex =  event.index)
                is UIEvent.UpdateProgress -> {
                    serviceHandler.onPlayerEvents(MediaEvent.UpdateProgress(event.newProgress))
                    progress = event.newProgress
                }
            }
        }
    }

    private fun mediaHandlerEvents() {
        viewModelScope.launch {
            serviceHandler.audioState.collectLatest { mediaState ->
                when (mediaState) {
                    is MediaState.Buffering -> calculateProgress(mediaState.progress)
                    is MediaState.CurrentPlaying -> currentSelectedMedia =
                        mediaList[mediaState.mediaIndex]

                    MediaState.Initial -> _uiState.value = UIState.Initial
                    is MediaState.PLaying -> isPlaying = mediaState.isPLaying
                    is MediaState.Progress -> calculateProgress(mediaState.progress)
                    is MediaState.Ready -> {
                        duration = mediaState.duration
                        _uiState.value = UIState.Ready
                    }
                }
            }
        }
    }

    private fun calculateProgress(currentProgress: Long) {
        progress = if (currentProgress > 0) {
            ((currentProgress.toFloat())/ duration.toFloat()) * 100f
        } else {
            0f
        }
        progressString = formatDuration(currentProgress)
    }

    private fun formatDuration(duration: Long): String {
        val minute = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = minute - (minute * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minute, seconds)
    }

    override fun onCleared() {
        viewModelScope.launch {
            serviceHandler.onPlayerEvents(MediaEvent.Stop)
        }
        super.onCleared()
    }

}

sealed interface UIEvent {
    data object PlayPause: UIEvent
    data class SelectedAudioChange(val index: Int): UIEvent
    data class SeekTo(val position: Float): UIEvent
    data object SeekToNext: UIEvent
    data object Backward: UIEvent
    data object Forward: UIEvent
    data class UpdateProgress(val newProgress: Float): UIEvent
}

sealed interface UIState {
    data object Initial: UIState
    data object Ready: UIState
}