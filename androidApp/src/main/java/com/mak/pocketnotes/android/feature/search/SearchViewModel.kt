package com.mak.pocketnotes.android.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mak.pocketnotes.domain.models.Genre
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.domain.models.PodcastEpisode
import com.mak.pocketnotes.domain.usecase.GetGenres
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SearchViewModel(
    private val getGenres: GetGenres
): ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

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
        return genres.isNotEmpty() && podcasts.isEmpty() && episodes.isEmpty()
    }
}