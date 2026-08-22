package com.mak.pocketnotes.android.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.core.common.models.ErrorType
import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.core.feature.domain.home.models.BestQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcastsParam
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import com.mak.pocketnotes.core.feature.domain.home.repository.CuratedPodcastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewmodel(
  private val bestPodcastsRepository: BestPodcastRepository,
  private val curatedPodcastsRepository: CuratedPodcastRepository,
) : ViewModel() {
  private val refreshTrigger = MutableSharedFlow<Boolean>(replay = 1).apply { tryEmit(false) }
  private val errorMsg = MutableStateFlow<ErrorType?>(null)

  internal val uiState: StateFlow<DiscoverScreenState> =
    combine(
      refreshBanner(),
      refreshBestPodcasts(),
      refreshCuratedPodcasts(),
      errorMsg,
    ) { bannerSection, bestSection, curatedSection, error ->
      DiscoverScreenState(
        isPullToRefreshing = bannerSection.isInFlight() || bestSection.isInFlight() || curatedSection.isInFlight(),
        bannerPodcastsSection = bannerSection,
        trendingPodcastsSection = bestSection,
        curatedPodcastsSection = curatedSection,
        errorType = error,
      )
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue =
        DiscoverScreenState(
          bannerPodcastsSection = SectionState.Loading,
          trendingPodcastsSection = SectionState.Loading,
          curatedPodcastsSection = SectionState.Loading,
          isPullToRefreshing = false,
        ),
    )

  private fun refreshCuratedPodcasts(): Flow<SectionState<List<CuratedPodcast>>> =
    refreshTrigger
      .flatMapLatest {
        curatedPodcastsRepository
          .refreshSection(
            CuratedPodcastsParam(
              forceRefresh = it,
            ),
          ).distinctUntilChanged()
      }.onEach { updateError(it) }

  private fun refreshBestPodcasts(): Flow<SectionState<List<Podcast>>> =
    refreshTrigger
      .flatMapLatest {
        bestPodcastsRepository
          .refreshSection(
            BestQueryParam(forceRefresh = it),
          ).distinctUntilChanged()
      }.onEach { updateError(it) }

  private fun refreshBanner(): Flow<SectionState<List<Podcast>>> =
    refreshTrigger
      .flatMapLatest {
        bestPodcastsRepository
          .refreshBannerSection(
            BestQueryParam(forceRefresh = it),
          ).distinctUntilChanged()
      }.onEach { updateError(it) }

  private fun updateError(state: SectionState<*>) {
    if (state is SectionState.Error) {
      errorMsg.update { state.type }
    }
  }

  fun refreshPodcasts() {
    viewModelScope.launch {
      refreshTrigger.emit(true)
    }
  }

  fun onErrorConsumed() {
    errorMsg.update { null }
  }
}

internal data class DiscoverScreenState(
  val bannerPodcastsSection: SectionState<List<Podcast>>,
  val trendingPodcastsSection: SectionState<List<Podcast>>,
  val curatedPodcastsSection: SectionState<List<CuratedPodcast>>,
  val isPullToRefreshing: Boolean,
  val errorType: ErrorType? = null,
) {
  /** True if any region is actively loading or mid a background refresh - used to know when
   * a PullToRefresh-triggered fetch has fully settled (see HomeViewModel). */
  internal fun hasSectionInFlight(): Boolean =
    bannerPodcastsSection.isInFlight() || trendingPodcastsSection.isInFlight() || curatedPodcastsSection.isInFlight()

  fun initialLoading(): Boolean =
    isInitialLoading(bannerPodcastsSection, trendingPodcastsSection, curatedPodcastsSection)
}

private fun isInitialLoading(
  banner: SectionState<*>,
  trending: SectionState<*>,
  curated: SectionState<*>,
): Boolean = banner.isInitial() && trending.isInitial() && curated.isInitial()

private fun SectionState<*>.isInitial(): Boolean =
  when (this) {
    is SectionState.Loading -> true
    is SectionState.Error<*> -> (this.cachedData as? Collection<*>)?.isEmpty() ?: true
    is SectionState.Success<*> -> (this.data as? Collection<*>)?.isEmpty() ?: true
    is SectionState.Empty -> true
  }
