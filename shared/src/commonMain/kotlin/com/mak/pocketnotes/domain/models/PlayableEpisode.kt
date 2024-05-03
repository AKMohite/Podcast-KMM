package com.mak.pocketnotes.domain.models

data class PlayableEpisode(
    val id: String,
    val title: String,
    val image: String,
    val track: String,
    val duration: Int
)
