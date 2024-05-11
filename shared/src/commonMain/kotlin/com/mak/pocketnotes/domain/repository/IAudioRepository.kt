package com.mak.pocketnotes.domain.repository

import com.mak.pocketnotes.domain.models.PlayableEpisode

interface IAudioRepository {

    val tracks: List<PlayableEpisode>

}