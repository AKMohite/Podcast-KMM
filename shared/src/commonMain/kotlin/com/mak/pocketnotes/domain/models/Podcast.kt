package com.mak.pocketnotes.domain.models

data class Podcast(
    val id: String,
    val description: String,
    val image: String,
    val listenScore: Int,
    val publisher: String,
    val thumbnail: String,
    val title: String,
    val totalEpisodes: Int,
    val type: String,
    val website: String
)