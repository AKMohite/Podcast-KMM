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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.common.navigation.AdaptiveScreenType
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

@Composable
internal fun HomeScreen(
    state: HomeScreenState,
    adaptiveScreenType: AdaptiveScreenType,
    gotoDetails: (String) -> Unit,
    loadNextPodcasts: (Boolean) -> Unit,
) {
    HomeContent(
        uiState = state,
        loadNextPodcasts = loadNextPodcasts,
        gotoDetails = gotoDetails,
        adaptiveScreenType = adaptiveScreenType
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    adaptiveScreenType: AdaptiveScreenType = AdaptiveScreenType.Compact,
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
                /*HomeCarousel(
                    modifier = Modifier.fillMaxWidth(),
                    podcasts = uiState.topPodcasts,
                    onPodcastClick = gotoDetails
                )*/
                HomeHeader(
                    modifier = Modifier.fillMaxWidth(),
                    podcasts = uiState.topPodcasts,
                    onPodcastClick = gotoDetails,
                    adaptiveScreenType = adaptiveScreenType
                )
            }
            item("best-podcasts") {
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

class HomeScreenStateProvider: PreviewParameterProvider<HomeScreenState> {

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