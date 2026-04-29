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
import com.mak.pocketnotes.android.ui.theme.adaptiveScreenInfo
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isMedium
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PodcastNavigationWrapper(
    startService: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mediaViewModel: MediaViewModel = koinViewModel()
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
        startRoute = Home,
        topLevelRoutes = setOf(Home, Search, Subscribed, Settings)
    )
    val navigator = remember { Navigator(navigationState) }

    val currentKey = navigationState.backStacks[navigationState.topLevelRoute]?.lastOrNull() ?: navigationState.topLevelRoute
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val gesturesEnabled = drawerState.isOpen || navLayoutType == NavigationSuiteType.NavigationDrawer
    val bottomBarItems = listOf(
        Home,
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
    Surface {
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
                                        .clickable { navigator.navigate(PodcastPlayer) },
                                    episode = mediaViewModel.currentSelectedMedia,
                                    play = { mediaViewModel.onUIEvents(UIEvent.PlayPause) },
                                    next = { mediaViewModel.onUIEvents(UIEvent.SeekToNext) },
                                    isMediaPlaying = mediaViewModel.isPlaying
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
                            AnimatedVisibility(visible = !isFullScreen && mediaViewModel.currentSelectedMedia.track.isNotBlank()) {
                                PermanentMinPlayer(
                                    modifier = Modifier
                                        .clickable { navigator.navigate(PodcastPlayer) },
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
                PodcastNavDisplay(
                    navigationState = navigationState,
                    navigator = navigator,
                    startService = startService,
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
                            .clickable { navigator.navigate(PodcastPlayer) },
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
