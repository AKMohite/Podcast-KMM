package com.mak.pocketnotes.android.feature.podcastdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.usecase.GetPodcast
import kotlinx.coroutines.launch

internal class PodcastDetailViewModel(
    val getPodcast: GetPodcast,
    podcastId: String
): ViewModel() {

    private var _uiState by mutableStateOf(PodcastDetailState())
    internal val uiState: PodcastDetailState
        get() = _uiState

    init {
        loadPodcast(podcastId)
    }

    private fun loadPodcast(podcastId: String) {
        _uiState = uiState.copy(loading = true)
        viewModelScope.launch {
            _uiState = try {
                val podcast = getPodcast(podcastId)
                uiState.copy(loading = false, podcast = podcast)
            } catch (error: Throwable) {
                uiState.copy(loading = false, errorMsg = error.localizedMessage)
            }
        }
    }

}


internal data class PodcastDetailState(
    val loading: Boolean = false,
    val podcast: Podcast? = null,
    val errorMsg: String? = null
)
