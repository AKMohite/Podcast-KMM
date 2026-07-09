package com.mak.pocketnotes.core.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PodcastRecommendationsDTO(
    val recommendations: List<PodcastDTO>? = emptyList()
)