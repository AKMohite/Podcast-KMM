package com.mak.pocketnotes.media

import com.mak.pocketnotes.domain.models.PodcastEpisode


/**
 * Pure Kotlin queue state manager — zero Android or Media3 dependencies.
 *
 * Every mutation returns a [QueueOperation] describing what changed so the
 * caller (ExoPlayerController) can mirror the operation onto the actual Player
 * without re-reading the whole list.
 *
 * This design keeps [QueueManager] trivially unit-testable.
 */

class QueueManager {

    private val _episodes = mutableListOf<PodcastEpisode>()
    private var _currentIndex: Int = -1

    val episodes: List<PodcastEpisode> get() = _episodes.toList()
    val currentIndex: Int get() = _currentIndex
    val currentEpisode: PodcastEpisode? get() = _episodes.getOrNull(currentIndex)
    val size: Int get() = _episodes.size
    val isEmpty: Boolean get() = _episodes.isEmpty()

    /**
     * Replace the entire queue and set [startIndex] as current.
     */
    fun setQueue(episodes: List<PodcastEpisode>, startIndex: Int = 0): QueueOperation.SetQueue {
        require(episodes.isNotEmpty()) { "Queue must not be empty" }
        require(startIndex in episodes.indices) { "startIndex $startIndex out of bounds" }
        _episodes.clear()
        _episodes.addAll(episodes)
        _currentIndex = startIndex
        return QueueOperation.SetQueue(episodes.toList(), startIndex)
    }

    fun clear(): QueueOperation.Clear {
        _episodes.clear()
        _currentIndex = -1
        return QueueOperation.Clear
    }

    /**
     * Append [episode] to the end of the queue.
     */
    fun addToEnd(episode: PodcastEpisode): QueueOperation.AddAt {
        _episodes.add(episode)
        return QueueOperation.AddAt(episode, _episodes.lastIndex)
    }

    /**
     * Insert [episode] immediately after the currently playing item.
     * If queue is empty the episode becomes index 0 and current.
     */
    fun addNext(episode: PodcastEpisode): QueueOperation.AddAt {
        val insertAt = if (_currentIndex < 0) 0 else (_currentIndex + 1).coerceAtMost(_episodes.size)
        _episodes.add(insertAt, episode)

        if (_currentIndex < 0) {
            _currentIndex = 0
        } else if (insertAt <= _currentIndex) {
            _currentIndex++
        }
        return QueueOperation.AddAt(episode, insertAt)
    }

    /**
     * Remove item at [index]. Adjusts [currentIndex] accordingly.
     * Returns null if [index] is out of range.
     */
    fun removeAt(index: Int): QueueOperation.RemoveAt? {
        if (index !in _episodes.indices) return null
        _episodes.removeAt(index)
        _currentIndex = when {
            _episodes.isEmpty() -> -1
            index < _currentIndex -> _currentIndex - 1
            index == _currentIndex -> _currentIndex.coerceAtMost(_episodes.lastIndex)
            else -> _currentIndex
        }
        return QueueOperation.RemoveAt(index)
    }

    /**
     * Move item from [from] to [to]. No-op if either index is out of range.
     */
    fun move(from: Int, to: Int): QueueOperation.Move? {
        if (from == to) return QueueOperation.Move(from, to)
        if ((from !in _episodes.indices) || (to !in _episodes.indices)) return null

        val item = _episodes.removeAt(from)
        _episodes.add(to, item)

        _currentIndex = when (_currentIndex) {
            from -> to
            in (minOf(from, to)..maxOf(from, to)) -> {
                if (from < to) _currentIndex - 1 else _currentIndex + 1
            }
            else -> _currentIndex
        }
        return QueueOperation.Move(from, to)
    }

    /** Advance to next; returns new index or -1 if already at end. */
    fun advanceToNext(): Int {
        return if (_currentIndex < _episodes.lastIndex) {
            _currentIndex += 1
            _currentIndex
        } else -1
    }

    /** Go back to previous; returns new index or -1 if already at start. */
    fun goToPrevious(): Int {
        return if (_currentIndex > 0) {
            _currentIndex -= 1
            _currentIndex
        } else -1
    }

    fun skipToIndex(index: Int): Boolean {
        if (index !in _episodes.indices) return false
        _currentIndex = index
        return true
    }
}


sealed class QueueOperation {
    data class SetQueue(val episodes: List<PodcastEpisode>, val startIndex: Int) : QueueOperation()
    data class AddAt(val episode: PodcastEpisode, val index: Int) : QueueOperation()
    data class RemoveAt(val index: Int) : QueueOperation()
    data class Move(val from: Int, val to: Int) : QueueOperation()
    data object Clear : QueueOperation()
}