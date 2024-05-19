package com.mak.pocketnotes.android.common.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mak.pocketnotes.android.common.Home
import com.mak.pocketnotes.android.common.PodcastDetail
import com.mak.pocketnotes.android.common.PodcastPlayer
import com.mak.pocketnotes.android.common.ScreenDestination
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import com.mak.pocketnotes.android.common.ui.MiniPlayer
import com.mak.pocketnotes.android.common.viewmodel.MediaViewModel
import com.mak.pocketnotes.android.common.viewmodel.UIEvent
import com.mak.pocketnotes.android.feature.home.HomeScreen
import com.mak.pocketnotes.android.feature.player.NowPlayingScreen
import com.mak.pocketnotes.android.feature.podcastdetail.PodcastDetailScreen
import com.mak.pocketnotes.domain.models.asPlayableEpisodes
import com.mak.pocketnotes.utils.sample.sampleEpisodes
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PodcastNav(
    startService: () -> Unit
) {
    val mediaViewModel: MediaViewModel = koinViewModel()
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    val snackbarHostState = remember { SnackbarHostState() }
//    val scope = rememberCoroutineScope()


    val isSystemDark = isSystemInDarkTheme()
    val statusBarColor = if (isSystemDark) MaterialTheme.colorScheme.primaryContainer else Color.Transparent

    SideEffect {
        systemUiController.setStatusBarColor(statusBarColor, darkIcons = !isSystemDark)
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination

    val bottomBarItems = listOf(
        Home,
        Search,
        Subscribed,
        Settings
    )
    val isFullScreen = ScreenDestination.isFullScreen(currentScreen?.route)
//    val isPlayerScreen = rememberSaveable { currentScreen?.hierarchy?.any { it.route == PodcastPlayer.route } == true }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (!isFullScreen) {
                Column {
                AnimatedVisibility(visible = mediaViewModel.isPlaying) {
                    MiniPlayer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 4.dp)
                            .clickable { navController.navigate(PodcastPlayer.route) },
                        episode = sampleEpisodes[0],
                        play = {},
                        next = {},
                        isMediaPlaying = mediaViewModel.isPlaying
                    )
                }
                    PodBottomNavigation(
                        currentScreen = currentScreen,
                        bottomBarItems = bottomBarItems,
                        onBottomNavigate = {
                            navController.navigate(it.routeWithArgs) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPaddings ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPaddings),
            startDestination = Home.routeWithArgs
        ) {
            composable(Home.routeWithArgs) {
                HomeScreen(
                    gotoDetails = { podcast ->
                        navController.navigate(
                            "${PodcastDetail.route}/${podcast.id}"
                        )
                    }
                )
            }

            composable(PodcastDetail.routeWithArgs) {
                val movieId = it.arguments?.getString("podcast_id") ?: ""
                PodcastDetailScreen(
                    movieId = movieId,
                    startPodcast = { episodes ->
                        startService()
                        mediaViewModel.loadMedia(episodes.asPlayableEpisodes())
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
                    episode = mediaViewModel.currentSelectedMedia
                )
            }

            composable(Search.routeWithArgs) {
                EmptyScreen(
                    Search.title
                )
            }
            composable(Subscribed.routeWithArgs) {
                EmptyScreen(Subscribed.title)
            }
            composable(Settings.routeWithArgs) {
                EmptyScreen(Settings.title)
            }
        }
    }
}

@Composable
private fun EmptyScreen(
    @StringRes title: Int
) {
    Text(text = "${stringResource(id = title)} Work in progress", style = MaterialTheme.typography.headlineSmall)
}