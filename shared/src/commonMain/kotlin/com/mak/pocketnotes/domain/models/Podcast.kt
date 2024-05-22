package com.mak.pocketnotes.domain.models

@kotlinx.serialization.Serializable
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
    val website: String,
    val recommendations: List<Podcast> = emptyList(),
    val episodes: List<PodcastEpisode> = emptyList()
)
