package com.app.podcast.domain.model

data class PodcastsState(
    val isLoading: Boolean = false,
    val podcasts: List<BestPodcast> = emptyList(),
    val error: String = ""
)