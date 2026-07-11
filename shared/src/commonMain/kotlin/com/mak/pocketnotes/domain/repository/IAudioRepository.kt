package com.mak.pocketnotes.domain.repository

import com.mak.pocketnotes.core.feature.domain.home.models.PlayableEpisode

interface IAudioRepository {

    val tracks: List<PlayableEpisode>

}