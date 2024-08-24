package com.mak.pocketnotes.android.feature.podcastdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.usecase.GetPodcast
import com.mak.pocketnotes.domain.usecase.GetPodcastRecommendations
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

internal class PodcastDetailViewModel(
    val getPodcast: GetPodcast,
    val podcastRecommendations: GetPodcastRecommendations,
    podcastId: String
): ViewModel() {

    private var _uiState by mutableStateOf(PodcastDetailState())
    internal val uiState: PodcastDetailState
        get() = _uiState

    init {
        loadPodcast(podcastId)
    }

    private fun loadPodcast(podcastId: String) {
        getPodcast(podcastId)
            .onStart {
                _uiState = uiState.copy(loading = true)
            }
            .onCompletion {
                _uiState = uiState.copy(loading = false, podcast = pod)
            }
            .launchIn(viewModelScope)
//        viewModelScope.launch {
//            _uiState = try {
//                val podcast = getPodcast(podcastId)
//                val recommendations = podcastRecommendations(podcastId)
//                uiState.copy(loading = false, podcast = podcast.copy(recommendations = recommendations))
//            } catch (error: Throwable) {
//                uiState.copy(loading = false, errorMsg = error.localizedMessage)
//            }
//        }
    }

}


internal data class PodcastDetailState(
    val loading: Boolean = false,
    val podcast: Podcast? = null,
    val errorMsg: String? = null
)
