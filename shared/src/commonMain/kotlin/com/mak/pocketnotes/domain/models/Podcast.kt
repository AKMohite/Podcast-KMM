package com.mak.pocketnotes.domain.models

@kotlinx.serialization.Serializable
data class Podcast(
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val thumbnail: String,
    val publisher: String,
//    val listenScore: Int = 0,
//    val totalEpisodes: Int = 0,
//    val type: String = "",
//    val website: String = "",
    val recommendations: List<Podcast> = emptyList(),
    val episodes: List<PodcastEpisode> = emptyList(),
    val genres: String = "Adventure, Action, Technology, Health"
)
