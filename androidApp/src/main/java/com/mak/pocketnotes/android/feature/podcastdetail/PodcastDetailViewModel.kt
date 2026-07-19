package com.mak.pocketnotes.android.feature.podcastdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.EpisodeRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.PodcastRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.RelatedPodcastRepository
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
): ViewModel() {
    private val _uiState = MutableStateFlow(PodcastDetailState(loading = true))
    internal val uiState: StateFlow<PodcastDetailState> = _uiState.asStateFlow()

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
            if (_uiState.value.episodes.isEmpty()) {
                loadEpisodes()
            }
        }.catch { e ->
            _uiState.update { it.copy(loading = false, errorMsg = e.message) }
        }.launchIn(viewModelScope)
    }

    fun loadMoreEpisodes() {
        if (_uiState.value.loadingMore) return
        val lastEpisode = _uiState.value.episodes.lastOrNull()
        val nextDate = lastEpisode?.nextEpisodeAt
        if (nextDate != null) {
            loadEpisodes(nextDate)
        }
    }

    private fun loadEpisodes(nextEpisodeDate: Long? = null) {
        _uiState.update { it.copy(loadingMore = true) }
        episodeRepository.refresh(Pair(podcastId, nextEpisodeDate))
            .onEach { newEpisodes ->
                _uiState.update { state ->
                    state.copy(
                        loadingMore = false,
                        episodes = (state.episodes + newEpisodes).distinctBy { it.id }
                    )
                }
            }.catch { e ->
                _uiState.update { it.copy(loadingMore = false, errorMsg = e.message) }
            }
            .launchIn(viewModelScope)
    }

}


internal data class PodcastDetailState(
    val loading: Boolean = false,
    val podcast: Podcast? = null,
    val episodes: List<PodcastEpisode> = emptyList(),
    val loadingMore: Boolean = false,
    val errorMsg: String? = null
)
