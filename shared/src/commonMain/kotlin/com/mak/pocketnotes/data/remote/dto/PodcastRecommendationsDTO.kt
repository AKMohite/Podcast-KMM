package com.mak.pocketnotes.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class PodcastRecommendationsDTO(
    val recommendations: List<PodcastDTO>? = emptyList()
)