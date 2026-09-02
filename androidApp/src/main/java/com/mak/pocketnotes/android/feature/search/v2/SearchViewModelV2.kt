package com.mak.pocketnotes.android.feature.search.v2

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mak.pocketnotes.core.common.coroutines.combine
import com.mak.pocketnotes.core.feature.domain.home.models.BestQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import com.mak.pocketnotes.core.feature.domain.search.models.Genre
import com.mak.pocketnotes.core.feature.domain.search.repository.GenreRepository
import com.mak.pocketnotes.core.feature.domain.search.repository.SearchRepository
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class SearchViewModelV2(
  private val bestPodcastsRepository: BestPodcastRepository,
  private val genreRepository: GenreRepository,
  private val searchRepository: SearchRepository,
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {

  val searchQuery = savedStateHandle.getStateFlow(SEARCH_QUERY, "")

  private val _screenState = MutableStateFlow(SearchScreenState.IDLE)
  val screenState = _screenState.asStateFlow()

  private val _remoteSearchResults = MutableStateFlow<PagingData<Podcast>>(PagingData.empty())
  val remoteSearchResults = _remoteSearchResults.asStateFlow()

  private val _isLoading = MutableStateFlow(false)

  @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
  private val suggestions = searchQuery
    .debounce(300.milliseconds)
    .distinctUntilChanged()
    .flatMapLatest { query ->
      if (query.isBlank()) {
        flowOf(emptyList())
      } else {
        searchRepository.getLocalSuggestions(query)
      }
    }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

  val uiState = combine(
    getForYouPodcasts(),
    getGenres(),
    getRecentSearches(),
    getTrendingSearches(),
    suggestions,
    _isLoading
  ) { forYou, genres, recent, trending, suggestions, isLoading ->
    SearchUiState(
      forYouPodcasts = forYou,
      genres = genres,
      recentSearches = recent,
      trendingSearches = trending,
      suggestions = suggestions,
      isLoading = isLoading
    )
  }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = SearchUiState()
    )

  fun onEvent(event: SearchUiEvent) {
    when (event) {
      is SearchUiEvent.QueryChange -> {
        savedStateHandle[SEARCH_QUERY] = event.query
      }

      is SearchUiEvent.SearchFocusChange -> {
        if (event.isFocused && _screenState.value == SearchScreenState.IDLE) {
          _screenState.value = SearchScreenState.ACTIVE
        }
      }

      SearchUiEvent.SearchBack -> {
        _screenState.value = SearchScreenState.IDLE
        savedStateHandle[SEARCH_QUERY] = ""
      }

      is SearchUiEvent.SearchSubmit -> {
        savedStateHandle[SEARCH_QUERY] = event.query
        _screenState.value = SearchScreenState.RESULTS
        performRemoteSearch(event.query)
        updateRecentSearches(event.query)
      }

      is SearchUiEvent.RecentSearchDelete -> {
        // TODO
      }

      SearchUiEvent.ClearRecentSearches -> {
        // TODO
      }

      SearchUiEvent.LoadMore -> {
        // Managed by Paging 3 in UI
      }
    }
  }

  private fun performRemoteSearch(query: String) {
    viewModelScope.launch {
      searchRepository.searchPodcasts(query)
        .cachedIn(viewModelScope)
        .collect { pagingData ->
          _remoteSearchResults.value = pagingData
        }
    }
  }

  private fun updateRecentSearches(query: String) {
    // TODO: Save to DataStore
  }

  //  TODO get saved recent searches locally
  private fun getRecentSearches() = flowOf(listOf("Android", "Kotlin", "Jetpack"))

  private fun getForYouPodcasts() = bestPodcastsRepository
    .refresh(BestQueryParam())

  private fun getGenres() = genreRepository
    .refresh()

  private fun getTrendingSearches() = bestPodcastsRepository
    .refresh(BestQueryParam())
    .map { podcasts -> podcasts.take(10).map { it.title } }

  companion object {
    private const val SEARCH_QUERY = "search_query"
  }
}

data class SearchUiState(
  val forYouPodcasts: List<Podcast> = emptyList(),
  val recentSearches: List<String> = emptyList(),
  val genres: List<Genre> = emptyList(),
  val trendingSearches: List<String> = emptyList(),
  val suggestions: List<Podcast> = emptyList(),
  val isLoading: Boolean = false,
  val error: String? = null
)

enum class SearchScreenState {
  IDLE, ACTIVE, RESULTS
}

sealed interface SearchUiEvent {
  data class QueryChange(val query: String) : SearchUiEvent
  data class SearchFocusChange(val isFocused: Boolean) : SearchUiEvent
  data object SearchBack : SearchUiEvent
  data class SearchSubmit(val query: String) : SearchUiEvent
  data class RecentSearchDelete(val query: String) : SearchUiEvent
  data object ClearRecentSearches : SearchUiEvent
  data object LoadMore : SearchUiEvent
}
