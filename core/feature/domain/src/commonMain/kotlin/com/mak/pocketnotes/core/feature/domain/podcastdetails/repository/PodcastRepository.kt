package com.mak.pocketnotes.core.feature.domain.podcastdetails.repository

import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import kotlinx.coroutines.flow.Flow

interface PodcastRepository {
    fun refresh(podcastId: String): Flow<Podcast>
    fun observePodcast(podcastId: String): Flow<Podcast>
}