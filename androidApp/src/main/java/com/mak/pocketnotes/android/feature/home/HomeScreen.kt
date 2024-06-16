package com.mak.pocketnotes.android.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.feature.home.views.BestPodcasts
import com.mak.pocketnotes.android.feature.home.views.CuratedPodcastRow
import com.mak.pocketnotes.android.feature.home.views.HomeHeader
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.util.md2.PullRefreshIndicator
import com.mak.pocketnotes.android.util.md2.pullRefresh
import com.mak.pocketnotes.android.util.md2.rememberPullRefreshState
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.utils.sample.samplePodcasts

@Composable
internal fun HomeScreen(
    state: HomeScreenState,
    gotoDetails: (String) -> Unit,
    loadNextPodcasts: (Boolean) -> Unit
) {
    HomeContent(
        uiState = state,
        loadNextPodcasts = loadNextPodcasts,
        gotoDetails = gotoDetails
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    uiState: HomeScreenState,
    loadNextPodcasts: (Boolean) -> Unit,
    gotoDetails: (String) -> Unit
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
        LazyColumn {
            item {
                HomeHeader(
                    modifier = Modifier.fillMaxWidth(),
                    podcasts = uiState.topPodcasts,
                    onPodcastClick = gotoDetails
                )
            }
            item {
                AnimatedVisibility(visible = uiState.podcasts.isNotEmpty()) {
                    BestPodcasts(
                        modifier = Modifier.fillMaxWidth(),
                        gotoDetails = gotoDetails,
                        podcasts = uiState.getSectionedPodcasts()
                    )
                }
            }
            items(
                items = uiState.curatedPodcasts,
                key = { podcast: CuratedPodcast -> podcast.id }
            ) { podcast ->
                CuratedPodcastRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    podcast = podcast,
                    goToDetails = gotoDetails
                )
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
                    podcasts = samplePodcasts,
                    topPodcasts = samplePodcasts.take(3),
                ),
                loadNextPodcasts = {},
                gotoDetails = {},
            )
        }
    }
}