package com.mak.pocketnotes.android.feature.search.v2

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.core.feature.domain.home.models.BestQueryParam
import com.mak.pocketnotes.core.feature.domain.home.models.Podcast
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import com.mak.pocketnotes.core.feature.domain.search.models.Genre
import com.mak.pocketnotes.core.feature.domain.search.repository.GenreRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

internal class SearchViewModelV2(
  private val bestPodcastsRepository: BestPodcastRepository,
  private val genreRepository: GenreRepository,
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {

  val searchResultsState = combine(
    getForYouPodcasts(),
    getGenres(),
    getRecentSearches()
  ) { forYouPodcasts, genres, recentSearches ->
    SearchResultsState(
      forYouPodcasts = forYouPodcasts,
      genres = genres,
      recentSearches = recentSearches
    )
  }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = SearchResultsState()
    )

  //  TODO get saved recent searches locally
  private fun getRecentSearches() = flowOf(listOf("Android", "Kotlin", "Jetpack"))

  private fun getForYouPodcasts() = bestPodcastsRepository
    .refresh(BestQueryParam())

  private fun getGenres() = genreRepository
    .refresh()
}

data class SearchResultsState(
  val forYouPodcasts: List<Podcast> = emptyList(),
  val recentSearches: List<String> = emptyList(),
  val genres: List<Genre> = emptyList()
)
