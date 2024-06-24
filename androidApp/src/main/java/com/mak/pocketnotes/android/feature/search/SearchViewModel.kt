package com.mak.pocketnotes.android.feature.search

import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.Genre
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.domain.usecase.GetGenres
import com.mak.pocketnotes.domain.usecase.RefreshBestPodcasts
import com.mak.pocketnotes.domain.usecase.SearchPodcast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SearchViewModel(
    private val getGenres: GetGenres,
    private val searchPodcast: SearchPodcast,
    private val getBestPodcasts: RefreshBestPodcasts
): ViewModel(), SearchActions {

    private val _state = MutableStateFlow(SearchState())
    override val state: StateFlow<SearchState> = _state.asStateFlow()

    init {
        getAllGenres()
    }

    private fun getAllGenres() {
        getGenres()
            .map { genres ->
                _state.update { it.copy(genres = genres) }
            }.launchIn(viewModelScope)
    }

    override fun onSearchTextChange(value: String) {
//        TODO Not yet implemented
    }

    override fun onGenreSelect(genre: Genre) {
        viewModelScope.launch {
            try {
                val resultPodcasts = getBestPodcasts(1, genre.id)
                _state.update {
                    it.copy(
                        genrePodcasts = resultPodcasts
                    )
                }
            } catch (t: Throwable) {
                _state.update { it.copy(error = t.message) }
            }
        }
    }

    override fun onSearchClick(searchText: String) {
        viewModelScope.launch {
            try {
                val results = searchPodcast(searchText)
                _state.update {
                    it.copy(
                        episodes = results.episodes,
                        podcasts = results.podcasts,
                        genrePodcasts = emptyList()
                    )
                }
            } catch (t: Throwable) {
                _state.update { it.copy(error = t.message) }
            }
        }
    }

    override fun onSearchFocusChanged(focusState: FocusState) {
    }

    override fun closeSearch() {
        viewModelScope.launch {
            _state.update { it.searchCleared() }
        }
    }
}

internal data class SearchState(
    val searchText: String = "",
    val selectedGenre: String = "",
    val genres: List<Genre> = emptyList(),
    val podcasts: List<Podcast> = emptyList(),
    val genrePodcasts: List<Podcast> = emptyList(),
    val episodes: List<PodcastEpisode> = emptyList(),
    val error: String? = null
) {
    fun canShowGenres(): Boolean {
        return genres.isNotEmpty() && (!arePodcastsAvailable() && !areEpisodesAvailable() && !areGenrePodcastsAvailable())
    }

    fun areEpisodesAvailable() = episodes.isNotEmpty()
    fun arePodcastsAvailable() = podcasts.isNotEmpty()
    fun areGenrePodcastsAvailable() = genrePodcasts.isNotEmpty()
    fun isResultAvailable() = arePodcastsAvailable() || areEpisodesAvailable()
    fun searchCleared(): SearchState {
        return this.copy(episodes = emptyList(), podcasts = emptyList())
    }
}