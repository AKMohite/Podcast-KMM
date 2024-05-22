package com.mak.pocketnotes.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CuratedPodcastDTO(
    @SerialName("id")
    val id: String? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("listen_score")
    val listenScore: Int? = null,
    @SerialName("listen_score_global_rank")
    val listenScoreGlobalRank: String? = null,
    @SerialName("listennotes_url")
    val listennotesUrl: String? = null,
    @SerialName("publisher")
    val publisher: String? = null,
    @SerialName("thumbnail")
    val thumbnail: String? = null,
    @SerialName("title")
    val title: String? = null
)