package com.mak.pocketnotes.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SectionPodcastDTO(
    @SerialName("description")
    val description: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("listennotes_url")
    val listennotesUrl: String? = null,
    @SerialName("podcasts")
    val podcasts: List<CuratedPodcastDTO>? = emptyList(),
    @SerialName("pub_date_ms")
    val pubDateMs: Long? = null,
    @SerialName("source_domain")
    val sourceDomain: String? = null,
    @SerialName("source_url")
    val sourceUrl: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("total")
    val total: Int? = null
)