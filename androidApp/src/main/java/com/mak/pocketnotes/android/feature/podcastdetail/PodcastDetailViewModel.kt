package com.mak.pocketnotes.android.feature.podcastdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.EpisodeRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.PodcastRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.RelatedPodcastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

internal class PodcastDetailViewModel(
  val podcastRepository: PodcastRepository,
  val relatedPodcastRepository: RelatedPodcastRepository,
  val episodeRepository: EpisodeRepository,
  private val podcastId: String
) : ViewModel() {
  private val _uiState = MutableStateFlow(PodcastDetailState(loading = true))
  internal val uiState: StateFlow<PodcastDetailState> = _uiState.asStateFlow()

  val episodesPagingData: Flow<PagingData<PodcastEpisode>> = episodeRepository
    .getEpisodesPagingV2(podcastId)
    .cachedIn(viewModelScope)

  init {
    loadPodcastDetails()
  }

  private fun loadPodcastDetails() {
    combine(
      podcastRepository.refresh(podcastId),
      relatedPodcastRepository.refresh(podcastId)
    ) { podcast, recommendations ->
      podcast.copy(recommendations = recommendations.related)
    }.onEach { podcast ->
      _uiState.update { it.copy(loading = false, podcast = podcast) }
    }.catch { e ->
      _uiState.update { it.copy(loading = false, errorMsg = e.message) }
    }.launchIn(viewModelScope)
  }
}

internal data class PodcastDetailState(
  val loading: Boolean = false,
  val podcast: Podcast? = null,
  val errorMsg: String? = null
)
