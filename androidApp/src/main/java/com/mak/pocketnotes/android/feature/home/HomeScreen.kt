package com.mak.pocketnotes.android.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.feature.home.views.HomeHeader
import com.mak.pocketnotes.android.feature.home.views.PodcastRow
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.util.md2.PullRefreshIndicator
import com.mak.pocketnotes.android.util.md2.pullRefresh
import com.mak.pocketnotes.android.util.md2.rememberPullRefreshState
import com.mak.pocketnotes.domain.models.Podcast
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
                LazyRow {
                    items(items = uiState.getSectionedPodcasts()) { podcasts ->
                        Column(
                            modifier = Modifier
                                .fillParentMaxWidth(0.9f)
                                .wrapContentHeight()
                        ) {
                            podcasts.forEach { podcast ->
                                PodcastRow(
                                    modifier = Modifier
                                        .clickable { gotoDetails(podcast.id) }
                                        .padding(4.dp),
                                    podcast = podcast
                                )
                            }
                        }
                    }
                }
            }
            items(
                items = uiState.podcasts,
                key = { podcast: Podcast -> podcast.id }
            ) { podcast ->
                PodcastRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable { gotoDetails(podcast.id) },
                    podcast = podcast
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
                    podcasts = samplePodcasts
                ),
                loadNextPodcasts = {},
                gotoDetails = {}
            )
        }
    }
}