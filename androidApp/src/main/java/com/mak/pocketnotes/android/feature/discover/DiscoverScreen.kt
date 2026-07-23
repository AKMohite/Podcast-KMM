package com.mak.pocketnotes.android.feature.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.window.core.layout.WindowSizeClass
import com.mak.pocketnotes.android.R
import com.mak.pocketnotes.android.common.Discover
import com.mak.pocketnotes.android.common.PodcastDetail
import com.mak.pocketnotes.android.common.navigation.Navigator
import com.mak.pocketnotes.android.feature.discover.components.bestpodcast.DiscoverBestPodcasts
import com.mak.pocketnotes.android.feature.discover.components.curatedpodcast.DiscoverCuratedPodcasts
import com.mak.pocketnotes.android.feature.discover.components.header.DiscoverHeader
import com.mak.pocketnotes.android.feature.discover.components.loading.DiscoverShimmer
import com.mak.pocketnotes.core.common.models.ErrorType
import com.mak.pocketnotes.core.common.models.SectionState
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
            refreshPodcasts = discoverViewmodel::refreshPodcasts,
            onErrorConsumed = discoverViewmodel::onErrorConsumed
        )
    }
}

@Composable
internal fun DiscoverScreen(
    state: DiscoverScreenState,
    gotoDetails: (String) -> Unit,
    refreshPodcasts: () -> Unit,
    onErrorConsumed: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalResources.current
    val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass

    LaunchedEffect(state.errorType) {
        state.errorType?.let { type ->
            onErrorConsumed()
            val result = snackbarHostState.showSnackbar(
                message = type.toUserMessage(),
                actionLabel = resources.getString(R.string.action_retry),
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                refreshPodcasts()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        DiscoverContent(
            modifier = Modifier.padding(padding),
            uiState = state,
            refreshPodcasts = refreshPodcasts,
            gotoDetails = gotoDetails,
            sizeClass = sizeClass
        )
    }
}

internal fun ErrorType.toUserMessage(): String {
    return when (this) {
        ErrorType.NOT_FOUND -> "Podcast not found"
        ErrorType.SERVER_ERROR -> "Server error"
        ErrorType.UNAUTHORIZED -> "Unauthorized"
        ErrorType.NO_CONNECTIVITY -> "No connectivity"
        ErrorType.PARSE -> "Parse error"
        ErrorType.UNKNOWN -> "Unknown error"
    }
}

@Composable
private fun DiscoverContent(
    modifier: Modifier = Modifier,
    uiState: DiscoverScreenState,
    refreshPodcasts: () -> Unit,
    gotoDetails: (String) -> Unit,
    sizeClass: WindowSizeClass
) {
    PullToRefreshBox(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        onRefresh = { refreshPodcasts() },
        isRefreshing = uiState.isPullToRefreshing,
    ) {
        if (uiState.initialLoading()) {
            DiscoverShimmer(sizeClass = sizeClass)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // Banner Section
                renderSection(
                    state = uiState.bannerPodcastsSection,
                    onSuccess = { podcasts ->
                        DiscoverHeader(
                            modifier = Modifier.fillMaxWidth(),
                            podcasts = podcasts,
                            onPodcastClick = gotoDetails,
                            sizeClass = sizeClass
                        )
                    }
                )

                // Trending Section
                renderSection(
                    state = uiState.trendingPodcastsSection,
                    onSuccess = { podcasts ->
                        DiscoverBestPodcasts(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            gotoDetails = gotoDetails,
                            podcasts = podcasts,
                            sizeClass = sizeClass
                        )
                    }
                )

                // Curated Sections
                val curatedState = uiState.curatedPodcastsSection
                val curatedData = when (curatedState) {
                    is SectionState.Success -> curatedState.data
                    is SectionState.Error -> curatedState.cachedData ?: emptyList()
                    else -> emptyList()
                }

                if (curatedData.isNotEmpty()) {
                    items(
                        items = curatedData,
                        key = { category -> category.id },
                        contentType = { "curated_podcast" }
                    ) { podcast ->
                        DiscoverCuratedPodcasts(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            podcastSection = podcast,
                            goToDetails = gotoDetails,
                            sizeClass = sizeClass
                        )
                    }
                } else if (curatedState is SectionState.Empty) {
                    item {
                        EmptyStateMessage("No curated podcasts found.")
                    }
                }
            }
        }
    }
}

private fun <T> LazyListScope.renderSection(
    state: SectionState<T>,
    onSuccess: @Composable (T) -> Unit
) {
    when (state) {
        is SectionState.Success -> item { onSuccess(state.data) }
        is SectionState.Error -> {
            state.cachedData?.let { item { onSuccess(it) } }
        }

        else -> {}
        /*is SectionState.Empty -> item { EmptyStateMessage("No podcasts to listen") }
        is SectionState.Loading -> { *//* Handled by initialLoading at screen level *//* }*/
    }
}

@Composable
private fun EmptyStateMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
    }
}

/*
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
}*/
