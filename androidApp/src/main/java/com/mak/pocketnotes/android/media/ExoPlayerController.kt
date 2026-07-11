package com.mak.pocketnotes.android.media


import android.content.ComponentName
import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.domain.models.PlayerError
import com.mak.pocketnotes.domain.models.PlayerState
import com.mak.pocketnotes.domain.models.RepeatMode
import com.mak.pocketnotes.media.PlayerController
import com.mak.pocketnotes.media.QueueManager
import com.mak.pocketnotes.media.QueueOperation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

internal class ExoPlayerController(
    private val context: Context,
    private val queueManager: QueueManager,
) : PlayerController {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _state = MutableStateFlow(PlayerState())
    override val playerState: StateFlow<PlayerState> = _state.asStateFlow()

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null

    private var positionJob: Job? = null
    private var sleepTimerJob: Job? = null

    // ─────────────────────────────────────────────────────────────────────
    // Init — connect to PodcastMediaService asynchronously
    // ─────────────────────────────────────────────────────────────────────

    init {
        scope.launch {
            val token = SessionToken(
                context,
                ComponentName(context, PodcastMediaService::class.java),
            )
            controllerFuture = MediaController.Builder(context, token).buildAsync()
            try {
                controller = controllerFuture!!.await()
                controller!!.addListener(playerListener)
                startPositionPolling()
            } catch (e: Exception) {
                _state.update { it.copy(error = PlayerError.PlaybackError("Failed to connect: ${e.message}")) }
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Media3 Player.Listener
    // ─────────────────────────────────────────────────────────────────────

    private val playerListener = object : Player.Listener {

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _state.update { it.copy(isPlaying = isPlaying) }
            if (isPlaying) startPositionPolling() else positionJob?.cancel()
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            _state.update {
                it.copy(isLoading = playbackState == Player.STATE_BUFFERING)
            }
            if (playbackState == Player.STATE_READY) {
                _state.update { it.copy(durationMs = controller?.duration?.coerceAtLeast(0) ?: 0L) }
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val idx = controller?.currentMediaItemIndex ?: return
            queueManager.skipToIndex(idx)
            _state.update { state ->
                state.copy(
                    currentEpisode = queueManager.currentEpisode,
                    currentQueueIndex = idx,
                    queue = queueManager.episodes,
                    positionMs = 0L,
                    durationMs = controller?.duration?.coerceAtLeast(0) ?: 0L,
                    error = null,
                )
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            _state.update {
                it.copy(
                    isPlaying = false,
                    error = PlayerError.PlaybackError(error.message ?: "Unknown playback error"),
                )
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            _state.update {
                it.copy(
                    repeatMode = when (repeatMode) {
                        Player.REPEAT_MODE_ONE -> RepeatMode.ONE
                        Player.REPEAT_MODE_ALL -> RepeatMode.ALL
                        else -> RepeatMode.NONE
                    },
                )
            }
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            _state.update { it.copy(isShuffleEnabled = shuffleModeEnabled) }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Playback
    // ─────────────────────────────────────────────────────────────────────

    override fun playEpisode(episode: PodcastEpisode) {
        val op = queueManager.setQueue(listOf(episode), startIndex = 0)
        applySetQueue(op)
        controller?.prepare()
        controller?.play()
    }

    override fun playQueue(episodes: List<PodcastEpisode>, startIndex: Int) {
        val op = queueManager.setQueue(episodes, startIndex)
        applySetQueue(op)
        controller?.prepare()
        controller?.play()
    }

    override fun pause() {
        controller?.pause()
    }

    override fun resume() {
        controller?.play()
    }

    override fun togglePlayPause() {
        if (_state.value.isPlaying) pause() else resume()
    }

    // ─────────────────────────────────────────────────────────────────────
    // Seeking
    // ─────────────────────────────────────────────────────────────────────

    override fun seekTo(positionMs: Long) {
        val clamped = positionMs.coerceIn(0L, _state.value.durationMs)
        controller?.seekTo(clamped)
        _state.update { it.copy(positionMs = clamped) }
    }

    override fun skipForward(ms: Long) {
        seekTo(_state.value.positionMs + ms)
    }

    override fun skipBackward(ms: Long) {
        seekTo(_state.value.positionMs - ms)
    }

    // ─────────────────────────────────────────────────────────────────────
    // Navigation
    // ─────────────────────────────────────────────────────────────────────

    override fun skipToNext() {
        controller?.seekToNextMediaItem()
    }

    override fun skipToPrevious() {
        if (_state.value.positionMs > RESTART_THRESHOLD_MS) {
            seekTo(0L)
        } else {
            controller?.seekToPreviousMediaItem()
        }
    }

    override fun skipToQueueItem(index: Int) {
        controller?.seekToDefaultPosition(index)
    }

    // ─────────────────────────────────────────────────────────────────────
    // Queue mutations
    // ─────────────────────────────────────────────────────────────────────

    override fun addToQueueEnd(episode: PodcastEpisode) {
        val op = queueManager.addToEnd(episode)
        controller?.addMediaItem(op.index, episode.toMediaItem())
        _state.update { it.copy(queue = queueManager.episodes) }
    }

    override fun addToQueueNext(episode: PodcastEpisode) {
        val op = queueManager.addNext(episode)
        controller?.addMediaItem(op.index, episode.toMediaItem())
        _state.update {
            it.copy(
                queue = queueManager.episodes,
                currentQueueIndex = queueManager.currentIndex,
                currentEpisode = queueManager.currentEpisode,
            )
        }
    }

    override fun removeFromQueue(index: Int) {
        val op = queueManager.removeAt(index) ?: return
        controller?.removeMediaItem(op.index)
        _state.update {
            it.copy(
                queue = queueManager.episodes,
                currentQueueIndex = queueManager.currentIndex,
                currentEpisode = queueManager.currentEpisode,
            )
        }
    }

    override fun moveQueueItem(from: Int, to: Int) {
        val op = queueManager.move(from, to) ?: return
        controller?.moveMediaItem(op.from, op.to)
        _state.update {
            it.copy(
                queue = queueManager.episodes,
                currentQueueIndex = queueManager.currentIndex,
            )
        }
    }

    override fun clearQueue() {
        queueManager.clear()
        controller?.clearMediaItems()
        _state.update { PlayerState() }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Playback settings
    // ─────────────────────────────────────────────────────────────────────

    override fun setPlaybackSpeed(speed: Float) {
        controller?.setPlaybackSpeed(speed)
        _state.update { it.copy(playbackSpeed = speed) }
    }

    override fun toggleShuffle() {
        val enabled = !_state.value.isShuffleEnabled
        controller?.shuffleModeEnabled = enabled
        _state.update { it.copy(isShuffleEnabled = enabled) }
    }

    override fun cycleRepeatMode() {
        val next = _state.value.repeatMode.next()
        controller?.repeatMode = when (next) {
            RepeatMode.NONE -> Player.REPEAT_MODE_OFF
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        }
        _state.update { it.copy(repeatMode = next) }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Sleep timer
    // ─────────────────────────────────────────────────────────────────────

    override fun startSleepTimer(durationMs: Long) {
        sleepTimerJob?.cancel()
        _state.update { it.copy(isSleepTimerActive = true, sleepTimerRemainingMs = durationMs) }

        sleepTimerJob = scope.launch {
            var remaining = durationMs
            while (remaining > 0) {
                delay(1_000L)
                remaining -= 1_000L
                _state.update { it.copy(sleepTimerRemainingMs = remaining.coerceAtLeast(0)) }
            }
            pause()
            _state.update { it.copy(isSleepTimerActive = false, sleepTimerRemainingMs = 0L) }
        }
    }

    override fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        sleepTimerJob = null
        _state.update { it.copy(isSleepTimerActive = false, sleepTimerRemainingMs = 0L) }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Lifecycle
    // ─────────────────────────────────────────────────────────────────────

    override fun release() {
        positionJob?.cancel()
        sleepTimerJob?.cancel()
        controller?.removeListener(playerListener)
        controllerFuture?.let { MediaController.releaseFuture(it) }
        scope.cancel()
    }

    // ─────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────

    private fun applySetQueue(op: QueueOperation.SetQueue) {
        controller?.setMediaItems(
            op.episodes.map { it.toMediaItem() },
            op.startIndex,
            0L,
        )
        _state.update {
            it.copy(
                queue = op.episodes,
                currentEpisode = op.episodes.getOrNull(op.startIndex),
                currentQueueIndex = op.startIndex,
                positionMs = 0L,
                error = null,
            )
        }
    }

    private fun startPositionPolling() {
        positionJob?.cancel()
        positionJob = scope.launch {
            while (true) {
                delay(POSITION_POLL_INTERVAL_MS)
                val pos = controller?.currentPosition?.coerceAtLeast(0L) ?: continue
                val dur = controller?.duration?.coerceAtLeast(0L) ?: _state.value.durationMs
                _state.update { it.copy(positionMs = pos, durationMs = dur) }
            }
        }
    }

    private fun PodcastEpisode.toMediaItem(): MediaItem = MediaItem.Builder()
        .setMediaId(id)
        .setUri(audio)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title)
//                .setAlbumArtist(speaker)
                .setDisplayTitle(title)
                .setSubtitle(title)
                .setArtist(podcastId)
                .setArtworkUri(thumbnail.toUri())
                .setDurationMs(duration * 1_000L)
                .setDescription(description)
                .build(),
        )
        .build()

    companion object {
        private const val POSITION_POLL_INTERVAL_MS = 500L
        private const val RESTART_THRESHOLD_MS = 5_000L
    }
}