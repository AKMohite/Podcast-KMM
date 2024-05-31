package com.mak.pocketnotes.data.repository

import com.mak.pocketnotes.domain.models.PlayableEpisode
import com.mak.pocketnotes.domain.repository.IAudioRepository

class AudioRepository : IAudioRepository {

    private val _tracks = mutableListOf<PlayableEpisode>()
    override val tracks: List<PlayableEpisode>
        get() = _tracks

//    val episodes: List<PlayableEpisode>
//        field: MutableList<PlayableEpisode> = mutableListOf()

    fun addEpisode(episode: PlayableEpisode) {
        _tracks.add(episode)
    }

    fun removeEpisode(episode: PlayableEpisode) {
        _tracks.remove(episode)
    }

    fun clearPlaylist() {
        _tracks.clear()
    }

    fun moveSong(from: Int, to: Int) {
        if (from == to) return // moved to same position
        val episode = _tracks.removeAt(from)
        if (from < to)
            _tracks.add(to - 1, episode)
        else
            _tracks.add(to, episode)
    }

    fun getEpisodeAt(index: Int): PlayableEpisode? {
        if (index < 0 || index >= _tracks.size || _tracks.isEmpty()) return null
        return _tracks.getOrNull(index)
    }

    fun shufflePlaylist() {
        _tracks.shuffle()
    }
}
