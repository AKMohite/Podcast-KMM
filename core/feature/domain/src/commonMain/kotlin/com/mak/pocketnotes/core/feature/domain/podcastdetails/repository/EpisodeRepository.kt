package com.mak.pocketnotes.core.feature.domain.podcastdetails.repository

import androidx.paging.PagingData
import com.mak.pocketnotes.core.feature.domain.home.models.EpisodeQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
  fun refresh(params: EpisodeQueryParam): Flow<List<PodcastEpisode>>

  fun observeEpisodes(params: EpisodeQueryParam): Flow<List<PodcastEpisode>>

  fun getEpisodesPaging(podcastId: String): Flow<PagingData<PodcastEpisode>>

  fun getEpisodesPagingV2(podcastId: String): Flow<PagingData<PodcastEpisode>>
}
