package com.app.podcast.android.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.podcast.domain.interactor.GetBestPodcastUC
import com.app.podcast.domain.model.PodcastsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBestPodcast: GetBestPodcastUC
): ViewModel() {

    private val _state = mutableStateOf(PodcastsState())
    val state: State<PodcastsState> = _state

    init {
        getBestPodcasts()
    }

    private fun getBestPodcasts(forceReload: Boolean = false) {
        getBestPodcast(forceReload).onEach { dataState ->
            _state.value = state.value.copy(isLoading = dataState.isLoading)
            dataState.data?.let { podcasts ->
                _state.value = state.value.copy(podcasts = podcasts)
            }
            dataState.message?.let { msg ->
                _state.value = state.value.copy(error = msg)
            }
        }.launchIn(viewModelScope)
    }
}