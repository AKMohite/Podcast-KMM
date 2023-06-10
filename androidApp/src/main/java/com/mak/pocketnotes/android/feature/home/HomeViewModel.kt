package com.mak.pocketnotes.android.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.usecase.GetBestPodcasts
import kotlinx.coroutines.launch

class HomeViewModel(
    val getPodcast: GetBestPodcasts
): ViewModel() {

    private var _uiState by mutableStateOf(HomeScreenState())
    internal val uiState: HomeScreenState
        get() = _uiState
    private var currentPage = 1

    init {
        loadPodcasts()
    }

    fun loadPodcasts(forceReload: Boolean = false) {
        if (uiState.loading) return
        if (forceReload) currentPage = 1
        if (currentPage == 1) _uiState = uiState.copy(refreshing = true)
        viewModelScope.launch {
            _uiState = uiState.copy(loading = true)
            try {
                val resultPodcasts = getPodcast(currentPage)
                val podcasts = if (currentPage == 1) resultPodcasts else uiState.podcasts + resultPodcasts
                currentPage += 1
                _uiState = uiState.copy(
                    loading = false,
                    refreshing = false,
                    loadFinished = podcasts.isEmpty(),
                    podcasts = podcasts
                )
            } catch (error: Throwable) {
                _uiState = uiState.copy(
                    loading = false,
                    refreshing = false,
                    loadFinished = true,
                    errorMsg = error.localizedMessage
                )
            }
        }
    }

}

internal data class HomeScreenState(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val podcasts: List<Podcast> = emptyList(),
    val errorMsg: String? = null,
    val loadFinished: Boolean = false
)