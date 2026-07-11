package com.mak.pocketnotes.domain.models

import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode

data class SearchResults(
    val episodes: List<PodcastEpisode>,
    val podcasts: List<Podcast> = emptyList()
)
