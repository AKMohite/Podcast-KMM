package com.mak.pocketnotes.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.usecase.GetBestPodcasts
import com.mak.pocketnotes.domain.usecase.GetCuratedPodcasts
import com.mak.pocketnotes.domain.usecase.RefreshBestPodcasts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    val refreshPodcasts: RefreshBestPodcasts,
    val getBestPodcasts: GetBestPodcasts,
    val curatedPodcast: GetCuratedPodcasts
): ViewModel() {

    private var _uiState = MutableStateFlow(HomeScreenState())
    internal val uiState: StateFlow<HomeScreenState>
        get() = _uiState
    private var currentPage = 1

    init {
        loadPodcasts()
        refreshDiscover()
        observerPodcasts()
    }

    private fun observerPodcasts() {
        getBestPodcasts()
            .onEach { results ->
                val state = uiState.value
                val (topPodcasts, podcasts) = results.take(4) to results.drop(4)
                _uiState.update { current ->
                    current.copy(
                        loading = false,
                        refreshing = false,
                        loadFinished = podcasts.isEmpty(),
                        topPodcasts = topPodcasts,
                        podcasts = podcasts,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun refreshDiscover() {
        viewModelScope.launch {
            refreshPodcasts(1)
//        curatedPodcast(1)
        }
    }

    fun loadPodcasts(forceReload: Boolean = false) {
        val state = uiState.value
        if (state.loading) return
        if (forceReload) currentPage = 1
        viewModelScope.launch {
            if (currentPage == 1) _uiState.update { current ->
                current.copy(refreshing = true)
            }
            _uiState.update { current ->
                current.copy(loading = true)
            }
            try {
                val curatedPodcasts = curatedPodcast(1)
                currentPage += 1
                _uiState.update { current ->
                    current.copy(
                        loading = false,
                        refreshing = false,
                        curatedPodcasts = curatedPodcasts
                    )
                }
            } catch (error: Throwable) {
                _uiState.update { current ->
                    current.copy(
                        loading = false,
                        refreshing = false,
                        loadFinished = true,
                        errorMsg = error.localizedMessage
                    )
                }
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