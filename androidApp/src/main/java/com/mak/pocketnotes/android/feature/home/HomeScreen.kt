package com.mak.pocketnotes.android.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.feature.home.views.PodcastItem
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.util.md2.PullRefreshIndicator
import com.mak.pocketnotes.android.util.md2.pullRefresh
import com.mak.pocketnotes.android.util.md2.rememberPullRefreshState
import com.mak.pocketnotes.domain.models.Podcast
import com.mak.pocketnotes.utils.sample.samplePodcasts
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun HomeScreen(
    gotoDetails: (Podcast) -> Unit
) {
    val homeViewModel: HomeViewModel = koinViewModel()
    HomeContent(
        uiState = homeViewModel.uiState,
        loadNextPodcasts = homeViewModel::loadPodcasts,
        gotoDetails = gotoDetails
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeScreenState,
    loadNextPodcasts: (Boolean) -> Unit,
    gotoDetails: (Podcast) -> Unit
) {

    val refreshState = rememberPullRefreshState(
        refreshing = uiState.refreshing,
        onRefresh = { loadNextPodcasts(true) }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pullRefresh(state = refreshState)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(
                uiState.podcasts,
                key = { _, podcast: Podcast -> podcast.id }
            ) { index: Int, podcast: Podcast -> 
                PodcastItem(podcast = podcast, gotoDetails = gotoDetails)
                if (index >= uiState.podcasts.size - 1 && !uiState.loading && !uiState.loadFinished) {
                    LaunchedEffect(key1 = Unit, block = { loadNextPodcasts(false) })
                }
            }

            if (uiState.loading && uiState.podcasts.isNotEmpty()) {
                item(span = { GridItemSpan(2) }) { 
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = uiState.refreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    PocketNotesTheme {
        Surface {
            HomeContent(
                uiState = HomeScreenState(
                    podcasts = samplePodcasts
                ),
                loadNextPodcasts = {},
                gotoDetails = {}
            )
        }
    }
}