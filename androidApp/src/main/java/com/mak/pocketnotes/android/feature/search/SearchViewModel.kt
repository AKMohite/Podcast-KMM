package com.mak.pocketnotes.android.feature.search

import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.Genre
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.domain.usecase.GetGenres
import com.mak.pocketnotes.domain.usecase.SearchPodcast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SearchViewModel(
    private val getGenres: GetGenres,
    private val searchPodcast: SearchPodcast
): ViewModel(), SearchActions {

    private val _state = MutableStateFlow(SearchState())
    override val state: StateFlow<SearchState> = _state.asStateFlow()

    init {
        getAllGenres()
    }

    private fun getAllGenres() {
        viewModelScope.launch {
            try {
                val genres = getGenres()
                _state.update { it.copy(genres = genres) }
            } catch (t: Throwable) {
                _state.update { it.copy(error = t.message) }
            }
        }
    }

    override fun onSearchTextChange(value: String) {
//        TODO Not yet implemented
    }

    override fun onGenreSelect(genre: Genre) {
        TODO("Not yet implemented")
    }

    override fun onSearchClick(searchText: String) {
        viewModelScope.launch {
            try {
                val results = searchPodcast(searchText)
                _state.update {
                    it.copy(
                        episodes = results.episodes,
                        podcasts = results.podcasts
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
            _state.update { it.copy(episodes = emptyList(), podcasts = emptyList()) }
        }
    }
}

internal data class SearchState(
    val searchText: String = "",
    val selectedGenre: String = "",
    val genres: List<Genre> = emptyList(),
    val podcasts: List<Podcast> = emptyList(),
    val episodes: List<PodcastEpisode> = emptyList(),
    val error: String? = null
) {
    fun canShowGenres(): Boolean {
        return genres.isNotEmpty() && podcasts.isEmpty() && !areEpisodesAvailable()
    }

    fun areEpisodesAvailable() = episodes.isNotEmpty()
}