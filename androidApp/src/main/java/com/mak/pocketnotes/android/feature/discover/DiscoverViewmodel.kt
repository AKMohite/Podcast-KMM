package com.mak.pocketnotes.android.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.core.feature.domain.home.models.BestQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcastsParam
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import com.mak.pocketnotes.core.feature.domain.home.repository.CuratedPodcastRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DiscoverViewmodel(
    private val bestPodcastsRepository: BestPodcastRepository,
    private val curatedPodcastsRepository: CuratedPodcastRepository
) : ViewModel() {

    private val isRefreshing = MutableStateFlow(false)

    private val fetchBestPodcasts =
        bestPodcastsRepository.observePodcasts(BestQueryParam()).distinctUntilChanged()
            .map { best ->
        best.take(8).shuffled() to if (best.size > 4) best.drop(4) else best
    }

    private val fetchCuratedPodcasts = curatedPodcastsRepository.observePodcasts(
        CuratedPodcastsParam()
    ).distinctUntilChanged()


    private val _uiState = MutableStateFlow(DiscoverScreenState(loading = true))
    internal val uiState: StateFlow<DiscoverScreenState> = combine(
        isRefreshing.flatMapLatest { bestPodcastsRepository.refresh(BestQueryParam(forceRefresh = it)) },
        isRefreshing.flatMapLatest {
            curatedPodcastsRepository.refresh(
                CuratedPodcastsParam(
                    forceRefresh = it
                )
            )
        }
    ) { best, curated ->
        val (banner, trending) = best.take(8)
            .shuffled() to if (best.size > 4) best.drop(4) else best
        DiscoverScreenState(
            refreshing = false,
            topPodcasts = banner,
            podcasts = trending,
            curatedPodcasts = curated
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DiscoverScreenState(loading = true)
    )



    fun loadPodcasts(forceReload: Boolean = false) {
        isRefreshing.value = forceReload
    }

    fun onErrorConsumed() {
        _uiState.update { it.copy(errorMsg = null) }
    }
}

data class DiscoverScreenState(
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