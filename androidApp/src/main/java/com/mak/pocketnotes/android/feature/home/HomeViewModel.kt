package com.mak.pocketnotes.android.feature.home

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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val refreshBestPodcasts: RefreshBestPodcasts,
    private val refreshCuratedPodcasts: RefreshCuratedPodcasts,
    private val getBestPodcasts: BestPodcastsStore,
    private val getCuratedPodcasts: CuratedPodcastsStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenState(loading = true))
    internal val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    init {
        observePodcasts()
        loadPodcasts()
    }

    private fun observePodcasts() {
        combine(getBestPodcasts(), getCuratedPodcasts()) { best, curated ->
            val top = best.take(8).shuffled()
            val main = if (best.size > 4) best.drop(4) else best

            _uiState.update { current ->
                current.copy(
                    loading = false,
                    refreshing = false,
                    loadFinished = best.isNotEmpty(),
                    topPodcasts = top,
                    podcasts = main,
                    curatedPodcasts = curated
                )
            }
        }.launchIn(viewModelScope)
    }

    fun loadPodcasts(forceReload: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(refreshing = true, errorMsg = null) }
            try {
                val bestDeferred = async { refreshBestPodcasts(1) }
                val curatedDeferred = async { refreshCuratedPodcasts(1) }

                val results = awaitAll(bestDeferred, curatedDeferred)

                val errors = results.filterIsInstance<DomainResult.Error>()
                if (errors.isNotEmpty()) {
                    _uiState.update { it.copy(
                        errorMsg = errors.joinToString { e -> e.message.orEmpty() },
                        refreshing = false
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMsg = e.message ?: "An unexpected error occurred",
                        refreshing = false
                    )
                }
            }
        }
    }
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