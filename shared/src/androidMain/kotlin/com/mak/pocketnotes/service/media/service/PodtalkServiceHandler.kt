package com.mak.pocketnotes.service.media.service

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.mak.pocketnotes.domain.models.PlayableEpisode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class PodtalkServiceHandler(
    private val exoPlayer: Player
): Player.Listener, IServiceHandler {

    private val _audioState: MutableStateFlow<MediaState> = MutableStateFlow(MediaState.Initial)
    override val audioState: StateFlow<MediaState> = _audioState.asStateFlow()
    private var job: Job? = null

    init {
        exoPlayer.addListener(this)
    }

    override fun addMediaItem(playableEpisode: PlayableEpisode) {
        exoPlayer.setMediaItem(playableEpisode.asMediaItem())
        exoPlayer.prepare()
    }

    override fun addMediaItems(playableEpisodes: List<PlayableEpisode>) {
        val items = playableEpisodes.map { episode ->
            episode.asMediaItem()
        }
        exoPlayer.setMediaItems(items)
        exoPlayer.prepare()
    }

    override suspend fun onPlayerEvents(
        playerEvent: MediaEvent,
        selectedAudioIndex: Int,
        seekPosition: Long
    ) {
        when (playerEvent) {
            MediaEvent.Backward -> exoPlayer.seekBack()
            MediaEvent.Forward -> exoPlayer.seekForward()
            MediaEvent.PLayPause -> playPause()
            MediaEvent.SeekToNext -> exoPlayer.seekToNext()
            MediaEvent.SeekTo -> exoPlayer.seekTo(seekPosition)
            MediaEvent.SelectedAudioChange -> {
                when (selectedAudioIndex) {
                    exoPlayer.currentMediaItemIndex -> {
                        playPause()
                    }
                    else -> {
                        exoPlayer.seekToDefaultPosition(selectedAudioIndex)
                        _audioState.value = MediaState.PLaying(isPLaying = true)
                        exoPlayer.playWhenReady = true
                        startProgressUpdate()
                    }
                }
            }
            MediaEvent.Stop -> stopProgressUpdate()
            is MediaEvent.UpdateProgress -> {
                val newProgress = (exoPlayer.duration * playerEvent.newProgress).toLong()
                exoPlayer.seekTo(newProgress)
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
//        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _audioState.value = MediaState.Buffering(exoPlayer.currentPosition)
            ExoPlayer.STATE_READY -> _audioState.value = MediaState.Ready(exoPlayer.duration)
            else -> Unit
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _audioState.value = MediaState.PLaying(isPlaying)
        _audioState.value = MediaState.CurrentPlaying(exoPlayer.currentMediaItemIndex)
        if (isPlaying) {
//            TODO remove
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    private suspend fun playPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            stopProgressUpdate()
        } else {
            exoPlayer.play()
            _audioState.value = MediaState.PLaying(
                isPLaying = true
            )
            startProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() {
        job.run {
            while (true) {
                delay(500)
                _audioState.value = MediaState.Progress(exoPlayer.currentPosition)
            }
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _audioState.value = MediaState.PLaying(isPLaying = false)
    }

}

private fun PlayableEpisode.asMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setMediaId(id)
        .setUri(track)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setArtworkUri(Uri.parse(image))
                .setAlbumArtist(speaker)
                .setDisplayTitle(title)
                .setSubtitle(title)
                .build()
        )
        .build()
}

interface IServiceHandler {
    val audioState: StateFlow<MediaState>
    fun addMediaItem(playableEpisode: PlayableEpisode)
    fun addMediaItems(playableEpisodes: List<PlayableEpisode>)
    suspend fun onPlayerEvents(
        playerEvent: MediaEvent,
        selectedAudioIndex: Int = -1,
        seekPosition: Long = 0
    )
}

sealed interface MediaEvent {
    data object PLayPause: MediaEvent
    data object SelectedAudioChange: MediaEvent
    data object Backward: MediaEvent
    data object Forward: MediaEvent
    data object SeekToNext: MediaEvent
    data object Stop: MediaEvent
    data object SeekTo: MediaEvent

    data class UpdateProgress(val newProgress: Float): MediaEvent
}

sealed interface MediaState {
    data object Initial: MediaState
    data class Ready(val duration: Long): MediaState
    data class Progress(val progress: Long): MediaState
    data class Buffering(val progress: Long): MediaState
    data class PLaying(val isPLaying: Boolean): MediaState
    data class CurrentPlaying(val mediaIndex: Int): MediaState
}