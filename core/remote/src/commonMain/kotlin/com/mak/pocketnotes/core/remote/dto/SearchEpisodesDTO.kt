package com.mak.pocketnotes.core.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchEpisodesDTO(
    @SerialName("count")
    val count: Int?,
    @SerialName("next_offset")
    val nextOffset: Int?,
    @SerialName("results")
    val results: List<EpisodeDTO>?,
    @SerialName("took")
    val took: Double?,
    @SerialName("total")
    val total: Int?
)