package com.mak.pocketnotes.data.repository

import com.mak.pocketnotes.domain.models.PlayableEpisode
import com.mak.pocketnotes.domain.repository.IAudioRepository

class AudioRepository : IAudioRepository {

    private val _tracks = mutableListOf<PlayableEpisode>()
    override val tracks: List<PlayableEpisode>
        get() = _tracks



}