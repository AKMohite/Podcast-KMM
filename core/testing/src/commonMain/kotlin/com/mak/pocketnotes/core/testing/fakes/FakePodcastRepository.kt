package com.mak.pocketnotes.core.testing.fakes

import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.PodcastRepository
import com.mak.pocketnotes.core.testing.util.TestPodcastData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakePodcastRepository : PodcastRepository {
  var podcastToReturn: Podcast = TestPodcastData.samplePodcast
  private val subscribedIds = mutableSetOf<String>()
  private val subscribedFlow = MutableStateFlow<Set<String>>(emptySet())

  override fun refresh(podcastId: String): Flow<Podcast> =
    flowOf(podcastToReturn.copy(id = podcastId))

  override fun observePodcast(podcastId: String): Flow<Podcast> =
    flowOf(podcastToReturn.copy(id = podcastId))

  override fun isSubscribed(podcastId: String): Flow<Boolean> = subscribedFlow.map {
    it.contains(podcastId)
  }

  override suspend fun subscribe(podcastId: String) {
    subscribedIds.add(podcastId)
    subscribedFlow.value = subscribedIds.toSet()
  }

  override suspend fun unsubscribe(podcastId: String) {
    subscribedIds.remove(podcastId)
    subscribedFlow.value = subscribedIds.toSet()
  }

  override fun getSubscribedPodcasts(): Flow<List<Podcast>> = flowOf(listOf(podcastToReturn))
}
