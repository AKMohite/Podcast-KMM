package com.mak.pocketnotes.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CuratedPodcastsDTO(
    @SerialName("curated_lists")
    val curatedLists: List<SectionPodcastDTO>? = emptyList(),
    @SerialName("has_next")
    val hasNext: Boolean? = false,
    @SerialName("has_previous")
    val hasPrevious: Boolean? = false,
    @SerialName("next_page_number")
    val nextPageNumber: Int? = null,
    @SerialName("page_number")
    val pageNumber: Int? = null,
    @SerialName("previous_page_number")
    val previousPageNumber: Int? = 0,
    @SerialName("total")
    val total: Int? = null
)