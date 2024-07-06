package com.mak.pocketnotes.android.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.usecase.GetBestPodcasts
import com.mak.pocketnotes.domain.usecase.GetCuratedPodcasts
import com.mak.pocketnotes.domain.usecase.RefreshBestPodcasts
import com.mak.pocketnotes.domain.usecase.RefreshCuratedPodcasts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    val refreshBestPodcasts: RefreshBestPodcasts,
    val refreshCuratedPodcasts: RefreshCuratedPodcasts,
    val getBestPodcasts: GetBestPodcasts,
    val getCuratedPodcasts: GetCuratedPodcasts
): ViewModel() {

    private var _uiState = MutableStateFlow(HomeScreenState())
    internal val uiState: StateFlow<HomeScreenState>
        get() = _uiState

    init {
//        loadPodcasts()
        refreshDiscover()
        observerPodcasts()
    }

    private fun observerPodcasts() {
        combine(getBestPodcasts(), getCuratedPodcasts()) { bestPodcasts, curatedPodcasts ->
            val (topPodcasts, podcasts) = bestPodcasts.take(4) to bestPodcasts.drop(4)
            _uiState.update { current ->
                current.copy(
                    loading = false,
                    refreshing = false,
                    loadFinished = podcasts.isEmpty(),
                    topPodcasts = topPodcasts,
                    podcasts = podcasts,
                    curatedPodcasts = curatedPodcasts
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun refreshDiscover() {
        viewModelScope.launch {
            try {
//                TODO handle all exceptions
                refreshBestPodcasts(1)
                refreshCuratedPodcasts(1)
            } catch (t: Throwable) {
                Log.e("HomeViewModel", "refreshDiscover: ", t)
            }
        }
    }

    fun loadPodcasts(forceReload: Boolean = false) {
        refreshDiscover()
//        val state = uiState.value
//        if (state.loading) return
//        if (forceReload) currentPage = 1
//        viewModelScope.launch {
//            if (currentPage == 1) _uiState.update { current ->
//                current.copy(refreshing = true)
//            }
//            _uiState.update { current ->
//                current.copy(loading = true)
//            }
//            try {
//                val curatedPodcasts = refreshCuratedPodcasts(1)
//                currentPage += 1
//                _uiState.update { current ->
//                    current.copy(
//                        loading = false,
//                        refreshing = false,
//                        curatedPodcasts = curatedPodcasts
//                    )
//                }
//            } catch (error: Throwable) {
//                _uiState.update { current ->
//                    current.copy(
//                        loading = false,
//                        refreshing = false,
//                        loadFinished = true,
//                        errorMsg = error.localizedMessage
//                    )
//                }
//            }
//        }
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