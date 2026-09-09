package com.mak.pocketnotes.core.feature.domain.podcastdetails.repository

import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import kotlinx.coroutines.flow.Flow

interface PodcastRepository {
  fun refresh(podcastId: String): Flow<Podcast>

  fun observePodcast(podcastId: String): Flow<Podcast>

  fun isSubscribed(podcastId: String): Flow<Boolean>

  suspend fun subscribe(podcastId: String)

  suspend fun unsubscribe(podcastId: String)

  fun getSubscribedPodcasts(): Flow<List<Podcast>>
}
