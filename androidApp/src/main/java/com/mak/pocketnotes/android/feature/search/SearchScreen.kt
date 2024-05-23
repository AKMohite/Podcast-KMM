package com.mak.pocketnotes.android.feature.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.feature.search.views.GenreCells
import com.mak.pocketnotes.android.feature.search.views.SearchField
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.domain.models.Genre

@Composable
internal fun SearchScreen(
    state: SearchState,
    onGenreClick: (Genre) -> Unit
) {
    SearchContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onSearchTextChanged = {},
        onSearchFocusChanged = {},
        onKeyboardDoneClick = {},
        onGenreClick = onGenreClick
    )
}

@Composable
private fun SearchContent(
    modifier: Modifier = Modifier,
    state: SearchState,
    onSearchTextChanged: (String) -> Unit,
    onSearchFocusChanged: (FocusState) -> Unit,
    onKeyboardDoneClick: KeyboardActionScope.() -> Unit,
    onGenreClick: (Genre) -> Unit
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .onFocusChanged(onSearchFocusChanged)
    ) {
        SearchField(
            onSearchTextChanged = onSearchTextChanged,
            onKeyboardDoneClick = onKeyboardDoneClick
        )
        AnimatedVisibility(visible = state.canShowGenres()) {
            GenreCells(
                genres = state.genres,
                onGenreClick = onGenreClick
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
                onSearchTextChanged = {},
                onSearchFocusChanged = {},
                onKeyboardDoneClick = {},
                onGenreClick = {}
            )
        }
    }
}