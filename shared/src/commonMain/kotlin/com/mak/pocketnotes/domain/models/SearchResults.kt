package com.mak.pocketnotes.domain.models

data class SearchResults(
    val episodes: List<PodcastEpisode>,
    val podcasts: List<Podcast> = emptyList()
)
