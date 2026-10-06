package com.mak.pocketnotes.android.feature.podcastdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.EpisodeRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.usecase.GetPodcastDetailsUseCase
import com.mak.pocketnotes.core.feature.domain.podcastdetails.usecase.TogglePodcastSubscriptionUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class PodcastDetailViewModel(
  getPodcastDetailsUseCase: GetPodcastDetailsUseCase,
  private val togglePodcastSubscriptionUseCase: TogglePodcastSubscriptionUseCase,
  episodeRepository: EpisodeRepository,
  private val podcastId: String
) : ViewModel() {
  private val _uiState = MutableStateFlow(PodcastDetailState(loading = true))
  internal val uiState: StateFlow<PodcastDetailState> = _uiState.asStateFlow()

  val episodesPagingData: Flow<PagingData<PodcastEpisode>> = episodeRepository
    .getEpisodesPaging(podcastId)
    .cachedIn(viewModelScope)

  init {
    getPodcastDetailsUseCase(podcastId)
      .onEach { details ->
        _uiState.update {
          PodcastDetailState(
            loading = false,
            podcast = details.podcast,
            isSubscribed = details.isSubscribed
          )
        }
      }
      .catch { e ->
        _uiState.update { it.copy(loading = false, errorMsg = e.message) }
      }
      .launchIn(viewModelScope)
  }

  fun toggleSubscription() {
    viewModelScope.launch {
      togglePodcastSubscriptionUseCase(podcastId)
    }
  }
}

internal data class PodcastDetailState(
  val loading: Boolean = false,
  val podcast: Podcast? = null,
  val isSubscribed: Boolean = false,
  val errorMsg: String? = null
)
