package com.mak.pocketnotes.android.feature.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mak.pocketnotes.android.common.Discover
import com.mak.pocketnotes.android.common.PodcastDetail
import com.mak.pocketnotes.android.common.navigation.Navigator
import com.mak.pocketnotes.android.feature.discover.components.DiscoverHeader
import com.mak.pocketnotes.android.feature.discover.components.bestpodcast.DiscoverBestPodcasts
import com.mak.pocketnotes.android.feature.discover.components.curatedpodcast.DiscoverCuratedPodcasts
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.utils.sample.sampleCuratedPodcasts
import com.mak.pocketnotes.utils.sample.samplePodcasts
import org.koin.androidx.compose.koinViewModel


fun EntryProviderScope<NavKey>.discoverEntry(
    navigator: Navigator
) {
    entry<Discover> {
        val discoverViewmodel: DiscoverViewmodel = koinViewModel()
        val state by discoverViewmodel.uiState.collectAsStateWithLifecycle()
        DiscoverScreen(
            gotoDetails = { podcastId ->
                // this function would be in podcast detail api module
                navigator.navigate(PodcastDetail(podcastId))
            },
            state = state,
            loadNextPodcasts = discoverViewmodel::loadPodcasts,
            onErrorConsumed = discoverViewmodel::onErrorConsumed
        )
    }
}

@Composable
internal fun DiscoverScreen(
    state: DiscoverScreenState,
    gotoDetails: (String) -> Unit,
    loadNextPodcasts: (Boolean) -> Unit,
    onErrorConsumed: () -> Unit,
) {
    DiscoverContent(
        uiState = state,
        loadNextPodcasts = loadNextPodcasts,
        gotoDetails = gotoDetails,
        onErrorConsumed = onErrorConsumed
    )
}

@Composable
private fun DiscoverContent(
    modifier: Modifier = Modifier,
    uiState: DiscoverScreenState,
    loadNextPodcasts: (Boolean) -> Unit,
    gotoDetails: (String) -> Unit,
    onErrorConsumed: () -> Unit
) {
        PullToRefreshBox(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            onRefresh = { loadNextPodcasts(true) },
            isRefreshing = uiState.refreshing,
        ) {
            LazyColumn {
                item(key = "discover-header", contentType = "top_banner") {
                    DiscoverHeader(
                        modifier = Modifier.fillMaxWidth(),
                        podcasts = uiState.topPodcasts,
                        onPodcastClick = gotoDetails
                    )
                }
                item(key = "best-podcasts", contentType = "trending_podcast") {
                    DiscoverBestPodcasts(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        gotoDetails = gotoDetails,
                        podcasts = uiState.getSectionedPodcasts()
                    )
                }
                items(
                    items = uiState.curatedPodcasts,
                    key = { category -> category.id },
                    contentType = { "curated_podcast" }
                ) { podcast ->
                    DiscoverCuratedPodcasts(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        podcastSection = podcast,
                        goToDetails = gotoDetails
                    )
                }
            }
        }
}

private class DiscoverScreenStateProvider: PreviewParameterProvider<DiscoverScreenState> {

    val data = listOf(
        Pair(
            "Loading",
            DiscoverScreenState(
                loading = true,
            )
        ),
        Pair(
            "Refreshing",
            DiscoverScreenState(
                refreshing = true
            )
        ),
        Pair(
            "Empty",
            DiscoverScreenState()
        ),
        Pair(
            "Success",
            DiscoverScreenState(
                podcasts = samplePodcasts,
                topPodcasts = samplePodcasts.take(3),
                curatedPodcasts = sampleCuratedPodcasts
            )
        )
    )

    override val values: Sequence<DiscoverScreenState>
        get() = data.map { it.second }.asSequence()

    override fun getDisplayName(index: Int): String {
        return data[index].first
    }
}

@Preview
@PreviewScreenSizes
@Composable
private fun DiscoverContentPreview(
    @PreviewParameter(DiscoverScreenStateProvider::class) previewData: DiscoverScreenState
) {
    PocketNotesTheme {
        DiscoverContent(
            uiState = previewData,
            loadNextPodcasts = {},
            gotoDetails = {},
            onErrorConsumed = {}
        )
    }
}