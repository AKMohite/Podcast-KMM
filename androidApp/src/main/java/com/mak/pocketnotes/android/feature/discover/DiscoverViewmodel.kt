package com.mak.pocketnotes.android.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.core.feature.domain.home.models.BestQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcastsParam
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import com.mak.pocketnotes.core.feature.domain.home.repository.CuratedPodcastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewmodel(
    private val bestPodcastsRepository: BestPodcastRepository,
    private val curatedPodcastsRepository: CuratedPodcastRepository
) : ViewModel() {

    private val isRefreshing = MutableStateFlow(false)
    internal val uiState: StateFlow<DiscoverScreenState> = combine(
        refreshBanner(),
        refreshBestPodcasts(),
        refreshCuratedPodcasts()
    ) { bannerSection, bestSection, curatedSection ->
        DiscoverScreenState(
            isPullToRefreshing = false,
            bannerPodcastsSection = bannerSection,
            trendingPodcastsSection = bestSection,
            curatedPodcastsSection = curatedSection
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DiscoverScreenState(
            bannerPodcastsSection = SectionState.Loading,
            trendingPodcastsSection = SectionState.Loading,
            curatedPodcastsSection = SectionState.Loading,
            isPullToRefreshing = false,
        )
    )

    private fun refreshCuratedPodcasts(): Flow<SectionState<List<CuratedPodcast>>> =
        isRefreshing.flatMapLatest {
            curatedPodcastsRepository.refreshSection(
                CuratedPodcastsParam(
                    forceRefresh = it
                )
            )
        }

    private fun refreshBestPodcasts(): Flow<SectionState<List<Podcast>>> =
        isRefreshing.flatMapLatest {
            bestPodcastsRepository.refreshSection(
                BestQueryParam(forceRefresh = it)
            )
        }

    private fun refreshBanner(): Flow<SectionState<List<Podcast>>> = isRefreshing.flatMapLatest {
        bestPodcastsRepository.refreshBannerSection(
            BestQueryParam(forceRefresh = it)
        )
    }

    init {
        viewModelScope.launch {
            uiState.collect {
                if (it.hasSectionInFlight()) {
                    isRefreshing.update { true }
                } else {
                    isRefreshing.update { false }
                }
            }
        }
    }



    fun loadPodcasts(forceReload: Boolean = false) {
        isRefreshing.value = forceReload
    }

    fun onErrorConsumed() {
//        _uiState.update { it.copy(errorMsg = null) }
    }
}

internal data class DiscoverScreenState(
    val bannerPodcastsSection: SectionState<List<Podcast>>,
    val trendingPodcastsSection: SectionState<List<Podcast>>,
    val curatedPodcastsSection: SectionState<List<CuratedPodcast>>,
    val isPullToRefreshing: Boolean,
) {
    /** True if any region is actively loading or mid a background refresh - used to know when
     * a PullToRefresh-triggered fetch has fully settled (see HomeViewModel). */
    internal fun hasSectionInFlight(): Boolean =
        bannerPodcastsSection.isInFlight() || trendingPodcastsSection.isInFlight() || curatedPodcastsSection.isInFlight()

//    fun hasNoData(): Boolean =
}