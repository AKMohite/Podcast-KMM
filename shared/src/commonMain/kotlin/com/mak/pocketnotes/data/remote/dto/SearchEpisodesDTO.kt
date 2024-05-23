package com.mak.pocketnotes.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SearchEpisodesDTO(
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