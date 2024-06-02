package com.mak.pocketnotes.android.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.usecase.GetBestPodcasts
import com.mak.pocketnotes.domain.usecase.GetCuratedPodcasts
import com.mak.pocketnotes.domain.usecase.RefreshBestPodcasts
import kotlinx.coroutines.launch

class HomeViewModel(
    val refreshPodcasts: RefreshBestPodcasts,
    val getBestPodcasts: GetBestPodcasts,
    val curatedPodcast: GetCuratedPodcasts
): ViewModel() {

    private var _uiState by mutableStateOf(HomeScreenState())
    internal val uiState: HomeScreenState
        get() = _uiState
    private var currentPage = 1

    init {
        loadPodcasts()
        refreshDiscover()
    }

    private fun refreshDiscover() {
        viewModelScope.launch {
            refreshPodcasts(1)
//        curatedPodcast(1)
        }
    }

    fun loadPodcasts(forceReload: Boolean = false) {
        if (uiState.loading) return
        if (forceReload) currentPage = 1
        if (currentPage == 1) _uiState = uiState.copy(refreshing = true)
        viewModelScope.launch {
            _uiState = uiState.copy(loading = true)
            try {
                val resultPodcasts = getBestPodcasts()
                val curatedPodcasts = curatedPodcast(1)
                val topPodcasts = if (currentPage == 1) resultPodcasts.take(4) else uiState.topPodcasts
                val podcasts = if (currentPage == 1) resultPodcasts.drop(4) else uiState.podcasts + resultPodcasts
                currentPage += 1
                _uiState = uiState.copy(
                    loading = false,
                    refreshing = false,
                    loadFinished = podcasts.isEmpty(),
                    topPodcasts = topPodcasts,
                    podcasts = podcasts,
                    curatedPodcasts = curatedPodcasts
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
    val topPodcasts: List<Podcast> = emptyList(),
    val curatedPodcasts: List<CuratedPodcast> = emptyList(),
    val errorMsg: String? = null,
    val loadFinished: Boolean = false
) {
    fun getSectionedPodcasts(noOfRows: Int = 4) = podcasts.chunked(noOfRows)
}