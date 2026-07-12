package com.mak.pocketnotes.core.feature.domain.podcastdetails.repository

import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import kotlinx.coroutines.flow.Flow

interface RelatedPodcastRepository {
    fun refresh(podcastId: String): Flow<RelatedPodcasts>
    fun observe(podcastId: String): Flow<RelatedPodcasts>
}

data class RelatedPodcasts(
    val podcastId: String,
    val related: List<Podcast>
)