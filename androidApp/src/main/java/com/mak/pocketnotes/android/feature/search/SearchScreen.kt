package com.mak.pocketnotes.android.feature.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.feature.search.views.GenreCells
import com.mak.pocketnotes.android.feature.search.views.SearchField
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Genre
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun SearchScreen(
    state: SearchState,
    actions: SearchActions
) {
    SearchContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        actions = actions
    )
}

@Composable
private fun SearchContent(
    modifier: Modifier = Modifier,
    state: SearchState,
    actions: SearchActions
) {
    val controller = LocalSoftwareKeyboardController.current
    Column(
        modifier = modifier
            .padding(16.dp)
            .onFocusChanged(actions::onSearchFocusChanged)
    ) {
        SearchField(
            onKeyboardDoneClick = { searchQuery ->
                controller?.hide()
                actions.onSearchClick(searchQuery)
            }
        )
        AnimatedVisibility(visible = state.canShowGenres()) {
            GenreCells(
                genres = state.genres,
                onGenreClick = actions::onGenreSelect
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    PocketNotesTheme {
        Surface {
            SearchContent(
                state = SearchState(),
                actions = object : SearchActions {
                    override val state: StateFlow<SearchState>
                        get() = TODO("Not yet implemented")
                    override fun onSearchTextChange(value: String) = Unit
                    override fun onGenreSelect(genre: Genre) = Unit
                    override fun onSearchClick(searchText: String) = Unit
                    override fun onSearchFocusChanged(focusState: FocusState) = Unit
                }
            )
        }
    }
}