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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mak.pocketnotes.android.common.Discover
import com.mak.pocketnotes.android.common.PodcastPlayer
import com.mak.pocketnotes.android.common.ScreenDestination
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import com.mak.pocketnotes.android.common.ui.MiniPlayer
import com.mak.pocketnotes.android.common.ui.PermanentMinPlayer
import com.mak.pocketnotes.android.ui.theme.adaptiveScreenInfo
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isMedium
import com.mak.pocketnotes.domain.models.PlayableEpisode
import kotlinx.coroutines.launch

@Composable
internal fun PodcastNavigationWrapper(
    startService: () -> Unit,
    modifier: Modifier = Modifier
) {
    val adaptiveInfo = adaptiveScreenInfo()

    val sizeClass = adaptiveInfo.windowSizeClass
    val navLayoutType = when {
        adaptiveInfo.windowPosture.isTabletop -> NavigationSuiteType.NavigationBar
        sizeClass.isExpanded() -> NavigationSuiteType.NavigationDrawer
        sizeClass.isMedium() -> NavigationSuiteType.NavigationRail
        else -> NavigationSuiteType.NavigationBar
    }
    val snackbarHostState = remember { SnackbarHostState() }

    val navigationState = rememberNavigationState(
        startRoute = Discover,
        topLevelRoutes = setOf(Discover, Search, Subscribed, Settings)
    )
    val navigator = remember { Navigator(navigationState) }

    val currentKey = navigationState.backStacks[navigationState.topLevelRoute]?.lastOrNull() ?: navigationState.topLevelRoute
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val gesturesEnabled = drawerState.isOpen || navLayoutType == NavigationSuiteType.NavigationDrawer
    val bottomBarItems = listOf(
        Discover,
        Search,
        Subscribed,
        Settings
    )
    val isFullScreen = ScreenDestination.isFullScreen(currentKey)
    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }
    Surface(
        modifier = modifier
    ) {
        NavigationSuiteScaffoldLayout(
            layoutType = navLayoutType,
            navigationSuite = {
                when (navLayoutType) {
                    NavigationSuiteType.NavigationBar -> {
                        Column {
                            // TODO handle this properly
                            AnimatedVisibility(visible = !isFullScreen) {
                                MiniPlayer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(horizontal = 4.dp)
                                        .clickable { navigator.navigate(PodcastPlayer) },
                                    episode = PlayableEpisode.EMPTY,
                                    play = {  },
                                    next = {  },
                                    isMediaPlaying = true
                                )
                            }
                            AnimatedVisibility(visible = !isFullScreen) {
                                PodBottomNavigation(
                                    bottomBarItems = bottomBarItems,
                                    currentKey = navigationState.topLevelRoute,
                                    onBottomNavigate = { destination ->
                                        navigator.navigate(destination)
                                    }
                                )
                            }
                        }

                    }

                    NavigationSuiteType.NavigationRail -> {
                        PodModalWideNavigationRail(
                            bottomBarItems = bottomBarItems,
                            currentKey = navigationState.topLevelRoute,
                            onBottomNavigate = { destination ->
                                navigator.navigate(destination)
                            }
                        )
                    }

                    NavigationSuiteType.NavigationDrawer -> PodNavigationDrawer(
                        bottomBarItems = bottomBarItems,
                        currentKey = navigationState.topLevelRoute,
                        onBottomNavigate = { destination ->
                            navigator.navigate(destination)
                        },
                        bottomContent = {
                            AnimatedVisibility(visible = !isFullScreen) {
                                PermanentMinPlayer(
                                    modifier = Modifier
                                        .clickable { navigator.navigate(PodcastPlayer) },
                                    episode = PlayableEpisode.EMPTY,
                                    playPause = {  },
                                    isMediaPlaying = true,
                                    previousClick = {},
                                    nextClick = {  }
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
                PodcastNavDisplay(
                    navigationState = navigationState,
                    navigator = navigator,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                // TODO handle this properly
                AnimatedVisibility(visible = navLayoutType == NavigationSuiteType.NavigationRail && !isFullScreen) {
                    MiniPlayer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(Alignment.Bottom)
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .clickable { navigator.navigate(PodcastPlayer) },
                        episode = PlayableEpisode.EMPTY,
                        play = {  },
                        next = {  },
                        isMediaPlaying = true
                    )
                }
            }
        }
    }
}
