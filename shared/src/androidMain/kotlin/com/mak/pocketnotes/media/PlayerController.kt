package com.mak.pocketnotes.media

import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.domain.models.PlayerState
import kotlinx.coroutines.flow.StateFlow

/**
 * The single contract between the UI/ViewModels and the underlying media engine.
 */
interface PlayerController {

    /** Live player state. Collect in ViewModels via stateIn(WhileSubscribed). */
    val playerState: StateFlow<PlayerState>

    // ── Playback ──────────────────────────────────────────────────────────

    fun playEpisode(episode: PodcastEpisode)
    fun playQueue(episodes: List<PodcastEpisode>, startIndex: Int = 0)
    fun pause()
    fun resume()
    fun togglePlayPause()

    // ── Seeking ───────────────────────────────────────────────────────────

    fun seekTo(positionMs: Long)
    /** Jump forward by [ms] ms (default 30 s). */
    fun skipForward(ms: Long = 30_000L)
    /** Jump back by [ms] ms (default 10 s). */
    fun skipBackward(ms: Long = 10_000L)

    // ── Navigation ────────────────────────────────────────────────────────

    fun skipToNext()
    /**
     * If position > 5 s → seek to 0; else go to previous item.
     * Matches expected podcast app UX.
     */
    fun skipToPrevious()
    fun skipToQueueItem(index: Int)

    // ── Queue mutations ───────────────────────────────────────────────────

    fun addToQueueEnd(episode: PodcastEpisode)
    fun addToQueueNext(episode: PodcastEpisode)
    fun removeFromQueue(index: Int)
    fun moveQueueItem(from: Int, to: Int)
    fun clearQueue()

    // ── Playback settings ─────────────────────────────────────────────────

    fun setPlaybackSpeed(speed: Float)
    fun toggleShuffle()
    fun cycleRepeatMode()

    // ── Sleep timer ───────────────────────────────────────────────────────

    fun startSleepTimer(durationMs: Long)
    fun cancelSleepTimer()

    // ── Lifecycle ─────────────────────────────────────────────────────────

    /** Must be called when the owning scope is destroyed. */
    fun release()
}
