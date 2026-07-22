package com.mak.pocketnotes.core.feature.domain.home.models

data class EpisodeQueryParam(
    val podcastId: String,
    val nextEpisodeDate: Long? = null,
    val forceRefresh: Boolean = false
)
