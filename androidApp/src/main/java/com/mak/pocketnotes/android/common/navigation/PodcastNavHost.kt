package com.mak.pocketnotes.android.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mak.pocketnotes.android.common.Home
import com.mak.pocketnotes.android.common.PodcastDetail
import com.mak.pocketnotes.android.common.PodcastPlayer
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import com.mak.pocketnotes.android.common.viewmodel.MediaViewModel
import com.mak.pocketnotes.android.common.viewmodel.UIEvent
import com.mak.pocketnotes.android.feature.home.HomeScreen
import com.mak.pocketnotes.android.feature.home.HomeViewModel
import com.mak.pocketnotes.android.feature.player.NowPlayingScreen
import com.mak.pocketnotes.android.feature.podcastdetail.PodcastDetailScreen
import com.mak.pocketnotes.android.feature.podcastdetail.PodcastDetailViewModel
import com.mak.pocketnotes.android.feature.search.SearchScreen
import com.mak.pocketnotes.android.feature.search.SearchViewModel
import com.mak.pocketnotes.android.feature.settings.SettingsScreen
import com.mak.pocketnotes.domain.models.asPlayableEpisodes
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun PodcastNavHost(
    navController: NavHostController,
    startService: () -> Unit,
    adaptiveScreenType: AdaptiveScreenType,
    mediaViewModel: MediaViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Home.routeWithArgs
    ) {
        composable(Home.routeWithArgs) {
            val homeViewModel: HomeViewModel = koinViewModel()
            val state by homeViewModel.uiState.collectAsState()
            HomeScreen(
                gotoDetails = { podcastId ->
                    navController.navigate(
                        "${PodcastDetail.route}/$podcastId"
                    )
                },
                state = state,
                loadNextPodcasts = homeViewModel::loadPodcasts,
                adaptiveScreenType = adaptiveScreenType
            )
        }

        composable(PodcastDetail.routeWithArgs) {
            val movieId = it.arguments?.getString("podcast_id") ?: ""
            val detailViewModel: PodcastDetailViewModel = koinViewModel(
                parameters = { parametersOf(movieId) }
            )
            PodcastDetailScreen(
                state = detailViewModel.uiState,
                episodes = detailViewModel.episodesState,
                startPodcast = {
                    detailViewModel.episodesState.let { episodes ->
                        startService()
                        mediaViewModel.loadMedia(episodes.asPlayableEpisodes())
                    }
                },
                gotoDetails = { podcastId ->
                    navController.navigate(
                        "${PodcastDetail.route}/$podcastId"
                    )
                }
            )
        }

        composable(PodcastPlayer.routeWithArgs) {
            NowPlayingScreen(
                progress = mediaViewModel.progress,
                onCloseClick = { navController.popBackStack() },
                onSliderChange = { updateProgress ->
                    mediaViewModel.onUIEvents(UIEvent.SeekTo(updateProgress))
                },
                playPause = {
                    mediaViewModel.onUIEvents(UIEvent.PlayPause)
                },
                isMediaPLaying = mediaViewModel.isPlaying,
                episode = mediaViewModel.currentSelectedMedia,
                previousClick = { },
                nextClick = { mediaViewModel.onUIEvents(UIEvent.SeekToNext) },
                timeElapsed = mediaViewModel.progressString,
                totalDuration = "where is it?"
            )
        }

        composable(Search.routeWithArgs) {
            val viewModel: SearchViewModel = koinViewModel()
            val state by viewModel.state.collectAsState()
            SearchScreen(
                state = state,
                actions = viewModel,
                onPodcastClick = { podcastId ->
                    navController.navigate(
                        "${PodcastDetail.route}/$podcastId"
                    )
                }
            )
        }
        composable(Subscribed.routeWithArgs) {
            EmptyScreen(Subscribed.title)
        }
        composable(Settings.routeWithArgs) {
            SettingsScreen()
        }
    }
}