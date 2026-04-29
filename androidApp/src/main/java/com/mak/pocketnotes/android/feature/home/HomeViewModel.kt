package com.mak.pocketnotes.android.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.DomainResult
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.store.BestPodcastsStore
import com.mak.pocketnotes.domain.store.CuratedPodcastsStore
import com.mak.pocketnotes.domain.usecase.RefreshBestPodcasts
import com.mak.pocketnotes.domain.usecase.RefreshCuratedPodcasts
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    val refreshBestPodcasts: RefreshBestPodcasts,
    val refreshCuratedPodcasts: RefreshCuratedPodcasts,
    val getBestPodcasts: BestPodcastsStore,
    val getCuratedPodcasts: CuratedPodcastsStore
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
            val (topPodcasts, podcasts) = bestPodcasts.take(8).shuffled() to bestPodcasts.drop(4)
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
            _uiState.update { it.copy(refreshing = true) }
            val bestPodcasts = async { refreshBestPodcasts(1) }
            val curatedPodcasts = async { refreshCuratedPodcasts(1) }
            val (b, c) = awaitAll(bestPodcasts, curatedPodcasts)

            if (b is DomainResult.Error) {
                _uiState.update { it.copy(errorMsg = b.message) }
            } else if (c is DomainResult.Error) {
                _uiState.update { it.copy(errorMsg = c.message) }
            }
            _uiState.update { it.copy(refreshing = false) }
        }
    }

    fun loadPodcasts(forceReload: Boolean = false) {
        refreshDiscover()
    }

}

internal sealed interface HomeState {
    data object Idle : HomeState
    data object Loading : HomeState
    data object Empty : HomeState

    data class Success(
        val podcasts: List<Podcast> = emptyList(),
        val topPodcasts: List<Podcast> = emptyList(),
        val curatedPodcasts: List<CuratedPodcast> = emptyList(),
        val isPaginating: Boolean = false
    ) : HomeState {
        fun getSectionedPodcasts(noOfRows: Int = 4) = podcasts.chunked(noOfRows)
    }

    data class Error(val message: String) : HomeState
}

data class HomeScreenState(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val podcasts: List<Podcast> = emptyList(),
    val topPodcasts: List<Podcast> = emptyList(),
    val curatedPodcasts: List<CuratedPodcast> = emptyList(),
    val errorMsg: String? = null,
    val loadFinished: Boolean = false
) {
    fun getSectionedPodcasts() = podcasts
}