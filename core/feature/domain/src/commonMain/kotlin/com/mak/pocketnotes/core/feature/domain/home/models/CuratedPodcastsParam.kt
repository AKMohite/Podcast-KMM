package com.mak.pocketnotes.core.feature.domain.home.models

data class CuratedPodcastsParam(
    val page: Int = 1,
    val forceRefresh: Boolean = false
)