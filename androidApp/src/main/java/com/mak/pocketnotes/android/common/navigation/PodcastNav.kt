package com.mak.pocketnotes.android.common.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mak.pocketnotes.android.common.BottomDestination
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
import com.mak.pocketnotes.android.feature.home.HomeViewModel
import com.mak.pocketnotes.android.feature.player.NowPlayingScreen
import com.mak.pocketnotes.android.feature.podcastdetail.PodcastDetailScreen
import com.mak.pocketnotes.android.feature.podcastdetail.PodcastDetailViewModel
import com.mak.pocketnotes.domain.models.asPlayableEpisodes
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun PodcastNav(
    startService: () -> Unit,
    navigationType: NavigationType
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

    val bottomBarItems = listOf(
        Home,
        Search,
        Subscribed,
        Settings
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination
//    val isPlayerScreen = rememberSaveable { currentScreen?.hierarchy?.any { it.route == PodcastPlayer.route } == true }

    when (navigationType) {
        NavigationType.BOTTOM_NAV_BAR, NavigationType.NAVIGATION_RAIL -> {
            CompactMediumContent(
                snackbarHostState = snackbarHostState,
                navController = navController,
                mediaViewModel = mediaViewModel,
                navigationType = navigationType,
                currentScreen = currentScreen,
                startService = startService,
                bottomBarItems = bottomBarItems
            )
        }
        NavigationType.PERMANENT_NAVIGATION_DRAWER -> PodPermanentDrawer(
        bottomBarItems =bottomBarItems,
            onBottomNavigate = {
               onBottomItemTap(navController, it)
            },
            currentScreen = currentScreen
        ) {
           MainNavContent(
               navController = navController,
               startService = { },
               mediaViewModel = mediaViewModel
           )
        }
    }
}

@Composable
private fun CompactMediumContent(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    mediaViewModel: MediaViewModel,
    navigationType: NavigationType,
    currentScreen: NavDestination?,
    startService: () -> Unit,
    bottomBarItems: List<BottomDestination>
) {
    val isFullScreen = ScreenDestination.isFullScreen(currentScreen?.route)
    val isMiniPlayerAvailable = mediaViewModel.currentSelectedMedia.track.isNotBlank() && listOf(NavigationType.BOTTOM_NAV_BAR, NavigationType.NAVIGATION_RAIL).contains(navigationType)
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (!isFullScreen) {
                Column {
//                    TODO handle this properly
                    AnimatedVisibility(visible = isMiniPlayerAvailable) {
                        MiniPlayer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 4.dp)
                                .clickable { navController.navigate(PodcastPlayer.route) },
                            episode = mediaViewModel.currentSelectedMedia,
                            play = { mediaViewModel.onUIEvents(UIEvent.PlayPause) },
                            next = { mediaViewModel.onUIEvents(UIEvent.SeekToNext) },
                            isMediaPlaying = mediaViewModel.isPlaying
                        )
                    }
                    AnimatedVisibility(visible = navigationType == NavigationType.BOTTOM_NAV_BAR) {
                        PodBottomNavigation(
                            currentScreen = currentScreen,
                            bottomBarItems = bottomBarItems,
                            onBottomNavigate = {
                                onBottomItemTap(navController, it)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPaddings ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
        ) {
            AnimatedVisibility(visible = navigationType == NavigationType.NAVIGATION_RAIL) {
                PodNavRail(
                    bottomBarItems = bottomBarItems,
                    onBottomNavigate = { onBottomItemTap(navController, it) },
                    currentScreen = currentScreen
                )
            }
            MainNavContent(
                navController = navController,
                startService = startService,
                mediaViewModel = mediaViewModel
            )
        }
    }
}
private fun onBottomItemTap(
    navController: NavHostController,
    it: BottomDestination
) {
    navController.navigate(it.routeWithArgs) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun MainNavContent(
    navController: NavHostController,
    startService: () -> Unit,
    mediaViewModel: MediaViewModel
) {
    NavHost(
        navController = navController,
        modifier = Modifier.fillMaxSize(),
        startDestination = Home.routeWithArgs
    ) {
        composable(Home.routeWithArgs) {
            val homeViewModel: HomeViewModel = koinViewModel()
            HomeScreen(
                gotoDetails = { podcast ->
                    navController.navigate(
                        "${PodcastDetail.route}/${podcast.id}"
                    )
                },
                state = homeViewModel.uiState,
                loadNextPodcasts = homeViewModel::loadPodcasts
            )
        }

        composable(PodcastDetail.routeWithArgs) {
            val movieId = it.arguments?.getString("podcast_id") ?: ""
            val detailViewModel: PodcastDetailViewModel = koinViewModel(
                parameters = { parametersOf(movieId) }
            )
            PodcastDetailScreen(
                state = detailViewModel.uiState,
                startPodcast = {
                    detailViewModel.uiState.podcast?.episodes?.let { episodes ->
                        startService()
                        mediaViewModel.loadMedia(episodes.asPlayableEpisodes())
                    }
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

@Composable
private fun EmptyScreen(
    @StringRes title: Int
) {
    Text(text = "${stringResource(id = title)} Work in progress", style = MaterialTheme.typography.headlineSmall)
}