package com.mak.pocketnotes.android.common.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.mak.pocketnotes.android.common.BottomDestination
import com.mak.pocketnotes.android.common.Home
import com.mak.pocketnotes.android.common.PodcastPlayer
import com.mak.pocketnotes.android.common.ScreenDestination
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import com.mak.pocketnotes.android.common.ui.MiniPlayer
import com.mak.pocketnotes.android.common.ui.PermanentMinPlayer
import com.mak.pocketnotes.android.common.viewmodel.MediaViewModel
import com.mak.pocketnotes.android.common.viewmodel.UIEvent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

internal enum class AdaptiveScreenType {
    Compact, Medium, Expanded, Large, ExtraLarge
}

private fun WindowSizeClass.isCompact(): Boolean {
    return isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) || isHeightAtLeastBreakpoint(HEIGHT_DP_MEDIUM_LOWER_BOUND)
}

@Composable
internal fun PodcastNavigationWrapper(
    startService: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mediaViewModel: MediaViewModel = koinViewModel()
    val navController = rememberNavController()
    val adaptiveInfo = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)

    val sizeClass = adaptiveInfo.windowSizeClass
    val navLayoutType = when {
        adaptiveInfo.windowPosture.isTabletop -> NavigationSuiteType.NavigationBar
        sizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> NavigationSuiteType.NavigationDrawer
        sizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> NavigationSuiteType.NavigationRail
        else -> NavigationSuiteType.NavigationBar
    }
    val adaptiveScreenType = when {
        sizeClass.isWidthAtLeastBreakpoint(1600) -> {
            AdaptiveScreenType.ExtraLarge
        }

        sizeClass.isWidthAtLeastBreakpoint(1200) -> {
            AdaptiveScreenType.Large
        }

        sizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_EXPANDED_LOWER_BOUND) -> {
            AdaptiveScreenType.Expanded
        }

        sizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND) -> {
            AdaptiveScreenType.Medium
        }

        else -> {
            AdaptiveScreenType.Compact
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val gesturesEnabled = drawerState.isOpen || navLayoutType == NavigationSuiteType.NavigationDrawer
    val bottomBarItems = listOf(
        Home,
        Search,
        Subscribed,
        Settings
    )
    val isFullScreen = ScreenDestination.isFullScreen(currentScreen?.route)
    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }
    Surface {
        ModalNavigationDrawer(
            modifier = modifier,
            drawerState = drawerState,
            gesturesEnabled = gesturesEnabled,
            drawerContent = {}
        ) {
            NavigationSuiteScaffoldLayout(
                layoutType = navLayoutType,
                navigationSuite = {
                    when (navLayoutType) {
                        NavigationSuiteType.NavigationBar -> {
                            Column {
                                // TODO handle this properly
                                AnimatedVisibility(visible = !isFullScreen && mediaViewModel.currentSelectedMedia.track.isNotBlank()) {
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
                                AnimatedVisibility(visible = !isFullScreen) {
                                    PodBottomNavigation(
                                        bottomBarItems = bottomBarItems,
                                        currentScreen = currentScreen,
                                        onBottomNavigate = { destination ->
                                            navigateToDestination(
                                                navController,
                                                destination
                                            )
                                        }
                                    )
                                }
                            }

                        }

                        NavigationSuiteType.NavigationRail -> {
                            PodModalWideNavigationRail(
                                bottomBarItems = bottomBarItems,
//                                currentScreen = currentScreen,
                                onBottomNavigate = { destination ->
                                    navigateToDestination(
                                        navController,
                                        destination
                                    )
                                }
                            )
                        }

                        NavigationSuiteType.NavigationDrawer -> PodNavigationDrawer(
                            bottomBarItems = bottomBarItems,
                            currentScreen = currentScreen,
                            onBottomNavigate = { destination ->
                                navigateToDestination(
                                    navController,
                                    destination
                                )
                            },
                            bottomContent = {
                                AnimatedVisibility(visible = !isFullScreen && mediaViewModel.currentSelectedMedia.track.isNotBlank()) {
                                    PermanentMinPlayer(
                                        modifier = Modifier
                                            .clickable { navController.navigate(PodcastPlayer.route) },
                                        episode = mediaViewModel.currentSelectedMedia,
                                        playPause = { mediaViewModel.onUIEvents(UIEvent.PlayPause) },
                                        isMediaPlaying = mediaViewModel.isPlaying,
                                        previousClick = {},
                                        nextClick = { mediaViewModel.onUIEvents(UIEvent.SeekToNext) }
                                    )
                                }
                            }
                        )
                    }
                },
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    PodcastNavHost(
                        navController = navController,
                        startService = startService,
                        adaptiveScreenType = adaptiveScreenType,
                        mediaViewModel = mediaViewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    // TODO handle this properly
                    AnimatedVisibility(visible = navLayoutType == NavigationSuiteType.NavigationRail && !isFullScreen && mediaViewModel.currentSelectedMedia.track.isNotBlank()) {
                        MiniPlayer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(Alignment.Bottom)
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .clickable { navController.navigate(PodcastPlayer.route) },
                            episode = mediaViewModel.currentSelectedMedia,
                            play = { mediaViewModel.onUIEvents(UIEvent.PlayPause) },
                            next = { mediaViewModel.onUIEvents(UIEvent.SeekToNext) },
                            isMediaPlaying = mediaViewModel.isPlaying
                        )
                    }
                }
            }
        }
    }
}

private fun navigateToDestination(navController: NavController, destination: BottomDestination) {
    navController.navigate(destination.routeWithArgs) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
