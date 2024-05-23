package com.mak.pocketnotes.android.feature.search

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.feature.home.views.PodcastRow
import com.mak.pocketnotes.android.feature.podcastdetail.views.PodcastEpisodeItem
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
    BackHandler(enabled = !state.canShowGenres()) {
        actions.closeSearch()
    }
    Column (
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

        AnimatedVisibility(visible = state.isResultAvailable()) {
            ResultsContent(state)
        }
    }
}

@Composable
private fun ResultsContent(state: SearchState) {
    LazyColumn {
        item {
            AnimatedVisibility(visible = state.arePodcastsAvailable()) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.podcast),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow {
                        items(
                            items = state.podcasts,
                            key = { podcast -> podcast.id }
                        ) { podcast ->
                            PodcastRow(
                                modifier = Modifier
                                    .fillParentMaxWidth(0.9f),
                                podcast = podcast
                            )
                        }
                    }
                }
            }
        }
        item {
            AnimatedVisibility(visible = state.areEpisodesAvailable()) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.episode),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
        items(
            items = state.episodes,
            key = { episode -> episode.id }
        ) { episode ->
            PodcastEpisodeItem(
                episode = episode,
                showImage = true
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
                    override fun closeSearch() = Unit
                }
            )
        }
    }
}