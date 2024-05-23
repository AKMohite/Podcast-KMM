package com.mak.pocketnotes.android.feature.search

import androidx.compose.ui.focus.FocusState
import com.mak.pocketnotes.domain.models.Genre
import kotlinx.coroutines.flow.StateFlow

internal interface SearchActions {
    val state: StateFlow<SearchState>
    fun onSearchTextChange(value: String)
    fun onGenreSelect(genre: Genre)
    fun onSearchClick(searchText: String)
    fun onSearchFocusChanged(focusState: FocusState)
    fun closeSearch()
}
