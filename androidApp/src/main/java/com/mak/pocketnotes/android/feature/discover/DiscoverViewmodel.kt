package com.mak.pocketnotes.android.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.core.common.models.ErrorType
import com.mak.pocketnotes.core.common.models.SectionState
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.usecase.GetDiscoverFeedUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoverViewmodel(
  private val getDiscoverFeedUseCase: GetDiscoverFeedUseCase
) : ViewModel() {
  private val refreshTrigger = MutableSharedFlow<Boolean>(replay = 1).apply { tryEmit(false) }
  private val errorMsg = MutableStateFlow<ErrorType?>(null)

  internal val uiState: StateFlow<DiscoverScreenState> =
    combine(
      refreshTrigger.flatMapLatest { forceRefresh ->
        getDiscoverFeedUseCase(forceRefresh = forceRefresh).onEach { feed ->
          updateError(feed.bannerSection)
          updateError(feed.trendingSection)
          updateError(feed.curatedSection)
        }
      },
      errorMsg
    ) { feed, error ->
      DiscoverScreenState(
        isPullToRefreshing = feed.isPullToRefreshing,
        bannerPodcastsSection = feed.bannerSection,
        trendingPodcastsSection = feed.trendingSection,
        curatedPodcastsSection = feed.curatedSection,
        errorType = error
      )
    }.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = DiscoverScreenState(
        bannerPodcastsSection = SectionState.Loading,
        trendingPodcastsSection = SectionState.Loading,
        curatedPodcastsSection = SectionState.Loading,
        isPullToRefreshing = false
      )
    )

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
  val errorType: ErrorType? = null
) {
  internal fun hasSectionInFlight(): Boolean =
    bannerPodcastsSection.isInFlight() || trendingPodcastsSection.isInFlight() || curatedPodcastsSection.isInFlight()

  fun initialLoading(): Boolean =
    isInitialLoading(bannerPodcastsSection, trendingPodcastsSection, curatedPodcastsSection)
}

private fun isInitialLoading(
  banner: SectionState<*>,
  trending: SectionState<*>,
  curated: SectionState<*>
): Boolean = banner.isInitial() && trending.isInitial() && curated.isInitial()

private fun SectionState<*>.isInitial(): Boolean =
  when (this) {
    is SectionState.Loading -> true
    is SectionState.Error<*> -> (this.cachedData as? Collection<*>)?.isEmpty() ?: true
    is SectionState.Success<*> -> (this.data as? Collection<*>)?.isEmpty() ?: true
    is SectionState.Empty -> true
  }
