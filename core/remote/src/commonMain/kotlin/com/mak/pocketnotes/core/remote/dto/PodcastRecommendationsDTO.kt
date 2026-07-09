package com.mak.pocketnotes.core.remote.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class PodcastRecommendationsDTO(
    val recommendations: List<PodcastDTO>? = emptyList()
)