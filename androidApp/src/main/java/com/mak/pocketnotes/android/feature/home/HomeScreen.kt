package com.mak.pocketnotes.android.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mak.pocketnotes.android.common.Home
import com.mak.pocketnotes.android.common.PodcastDetail
import com.mak.pocketnotes.android.common.navigation.Navigator
import com.mak.pocketnotes.android.feature.home.views.BestPodcasts
import com.mak.pocketnotes.android.feature.home.views.CuratedPodcastRow
import com.mak.pocketnotes.android.feature.home.views.HomeHeader
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.android.util.md2.PullRefreshIndicator
import com.mak.pocketnotes.android.util.md2.pullRefresh
import com.mak.pocketnotes.android.util.md2.rememberPullRefreshState
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.utils.sample.sampleCuratedPodcasts
import com.mak.pocketnotes.utils.sample.samplePodcasts
import org.koin.androidx.compose.koinViewModel

fun EntryProviderScope<NavKey>.discoverEntry(
    navigator: Navigator
) {
    entry<Home> {
        val homeViewModel: HomeViewModel = koinViewModel()
        val state by homeViewModel.uiState.collectAsState()
        HomeScreen(
            gotoDetails = { podcastId ->
                // this function would be in podcast detail api module
                navigator.navigate(PodcastDetail(podcastId))
            },
            state = state,
            loadNextPodcasts = homeViewModel::loadPodcasts
        )
    }
}

@Composable
internal fun HomeScreen(
    state: HomeScreenState,
    gotoDetails: (String) -> Unit,
    loadNextPodcasts: (Boolean) -> Unit,
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
            item("home-header") {
                HomeHeader(
                    modifier = Modifier.fillMaxWidth(),
                    podcasts = uiState.topPodcasts,
                    onPodcastClick = gotoDetails
                )
            }
            item("best-podcasts") {
                BestPodcasts(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    gotoDetails = gotoDetails,
                    podcasts = uiState.getSectionedPodcasts()
                )
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
            refreshing = uiState.refreshing || uiState.loading,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

private class HomeScreenStateProvider: PreviewParameterProvider<HomeScreenState> {

    val data = listOf(
        Pair(
            "Loading",
            HomeScreenState(
                loading = true,
            )
        ),
        Pair(
            "Refreshing",
            HomeScreenState(
                refreshing = true
            )
        ),
        Pair(
            "Empty",
            HomeScreenState()
        ),
        Pair(
            "Success",
            HomeScreenState(
                podcasts = samplePodcasts,
                topPodcasts = samplePodcasts.take(3),
                curatedPodcasts = sampleCuratedPodcasts
            )
        )
    )

    override val values: Sequence<HomeScreenState>
        get() = data.map { it.second }.asSequence()

    override fun getDisplayName(index: Int): String {
        return data[index].first
    }
}

@Preview
@PreviewScreenSizes
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomeScreenStateProvider::class) previewData: HomeScreenState
) {
    PocketNotesTheme {
        Surface {
            HomeContent(
                uiState = previewData,
                loadNextPodcasts = {},
                gotoDetails = {},
            )
        }
    }
}