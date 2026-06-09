package com.mak.pocketnotes.android.common.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mak.pocketnotes.android.common.Discover
import com.mak.pocketnotes.android.common.PlayerQueue
import com.mak.pocketnotes.android.common.PodcastPlayer
import com.mak.pocketnotes.android.common.ScreenDestination
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import com.mak.pocketnotes.android.common.ui.PermanentMinPlayer
import com.mak.pocketnotes.android.feature.player.v2.NavEvent
import com.mak.pocketnotes.android.feature.player.v2.PlayerEvent
import com.mak.pocketnotes.android.feature.player.v2.PlayerExpansionViewModel
import com.mak.pocketnotes.android.feature.player.v2.PlayerScreen
import com.mak.pocketnotes.android.feature.player.v2.PlayerViewModel
import com.mak.pocketnotes.android.feature.player.v2.components.MiniPlayer
import com.mak.pocketnotes.android.ui.theme.adaptiveScreenInfo
import com.mak.pocketnotes.android.ui.theme.isExpanded
import com.mak.pocketnotes.android.ui.theme.isMedium
import com.mak.pocketnotes.domain.models.PlayableEpisode
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

internal object ScaffoldTestTags {
    const val BOTTOM_NAV      = "scaffold:bottom_nav"
    const val NAV_RAIL        = "scaffold:nav_rail"
    const val NAV_DRAWER      = "scaffold:nav_drawer"
    const val PLAYER_OVERLAY  = "scaffold:player_overlay"
    const val PLAYER_PANE     = "scaffold:player_pane"
    const val CONTENT_PANE    = "scaffold:content_pane"
    const val COLLAPSE_PLAYER = "scaffold:collapse_player"
}

@Composable
internal fun PodcastNavigationWrapper(
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

    val navigationState = rememberNavigationState(
        startRoute = Discover,
        topLevelRoutes = setOf(Discover, Search, Subscribed, Settings)
    )
    val navigator = remember { Navigator(navigationState) }

    val currentKey = navigationState.backStacks[navigationState.topLevelRoute]?.lastOrNull() ?: navigationState.topLevelRoute
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val bottomBarItems = listOf(
        Discover,
        Search,
        Subscribed,
        Settings
    )
    val isFullScreen = ScreenDestination.isFullScreen(currentKey)

    val playerViewModel: PlayerViewModel = koinViewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )
    val expansionViewModel: PlayerExpansionViewModel = koinViewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )
    val playerState by playerViewModel.playerState.collectAsStateWithLifecycle()
    val expansionState by expansionViewModel.expansionState.collectAsStateWithLifecycle()
    // ── Sync episode playing state → expansion state ──────────────────────
    //
    // When an episode starts (null → non-null) the mini bar should appear.
    // When playback is fully cleared the bar should vanish.
    LaunchedEffect(playerState.currentEpisode) {
        if (playerState.currentEpisode != null && !expansionState.isVisible) {
            expansionViewModel.onEpisodeStarted()
        } else if (playerState.currentEpisode == null) {
            expansionViewModel.onEpisodeStopped()
        }
    }

    // ── Notification / deep-link nav events ───────────────────────────────
    LaunchedEffect(Unit) {
        expansionViewModel.navEvents.collect { event ->
            when (event) {
                NavEvent.ShowPlayer -> {
                    expansionViewModel.expand()
                }

                is NavEvent.ShowEpisode -> {
//                    navController.navigateToEpisodeDetail(event.episodeId)
                    expansionViewModel.expand()
                }

                NavEvent.ShowQueue -> {
//                    if (windowSize.showInlineQueue) {
//                        // Expanded+: queue is already in the player pane
//                        expansionViewModel.expand()
//                    } else {
//                        // Compact/Medium: navigate to QueueRoute
//                        expansionViewModel.collapse()
//                        navController.navigateToQueue()
//                    }
                }
            }
        }
    }
    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }
    Surface(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
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
                                        //                                    modifier = Modifier
                                        //                                        .fillMaxWidth()
                                        //                                        .wrapContentHeight()
                                        //                                        .padding(horizontal = 4.dp)
                                        //                                        .clickable { navigator.navigate(PodcastPlayer) },
                                        state = playerState,
                                        onExpand = { expansionViewModel.expand() },
                                        onTogglePlayPause = { playerViewModel.onEvent(PlayerEvent.OnTogglePlayPause) }
                                    )
                                }
                                AnimatedVisibility(visible = !isFullScreen) {
                                    PodBottomNavigation(
                                        bottomBarItems = bottomBarItems,
                                        onBottomNavigate = { destination ->
                                            navigator.navigate(destination)
                                        },
                                        currentKey = navigationState.topLevelRoute,
                                        modifier = Modifier.testTag(ScaffoldTestTags.BOTTOM_NAV)
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
                                },
                                modifier = Modifier.testTag(ScaffoldTestTags.NAV_RAIL)
                            )
                        }

                        NavigationSuiteType.NavigationDrawer -> PodNavigationDrawer(
                            modifier = Modifier.testTag(ScaffoldTestTags.NAV_DRAWER),
                            bottomBarItems = bottomBarItems,
                            currentKey = navigationState.topLevelRoute,
                            onBottomNavigate = { destination ->
                                navigator.navigate(destination)
                            },
                            bottomContent = {
                                AnimatedVisibility(visible = playerState.currentEpisode != null) {
                                    PermanentMinPlayer(
                                        modifier = Modifier
                                            .clickable { navigator.navigate(PodcastPlayer) },
                                        episode = PlayableEpisode.EMPTY,
                                        playPause = { },
                                        isMediaPlaying = true,
                                        previousClick = {},
                                        nextClick = { }
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
                    AnimatedVisibility(visible = navLayoutType == NavigationSuiteType.NavigationRail) {
                        MiniPlayer(
                            //                        modifier = Modifier
                            //                            .fillMaxWidth()
                            //                            .wrapContentHeight(Alignment.Bottom)
                            //                            .weight(1f)
                            //                            .padding(horizontal = 4.dp)
                            //                            .clickable { navigator.navigate(PodcastPlayer) },
                            state = playerState,
                            onExpand = { expansionViewModel.expand() },
                            onTogglePlayPause = { playerViewModel.onEvent(PlayerEvent.OnTogglePlayPause) }
                        )
                    }
                }
            }

            // Full-player overlay
            AnimatedVisibility(
                visible = expansionState.isFullyExpanded,
                enter = slideInVertically(
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    initialOffsetY = { it },
                ) + fadeIn(tween(150)),
                exit = slideOutVertically(
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    targetOffsetY = { it },
                ) + fadeOut(tween(100)),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(ScaffoldTestTags.PLAYER_OVERLAY),
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column {
                        IconButton(
                            onClick = { expansionViewModel.collapse() },
                            modifier = Modifier
                                .padding(4.dp)
                                .testTag(ScaffoldTestTags.COLLAPSE_PLAYER),
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, "Collapse player")
                        }
                        PlayerScreen {
                            navigator.navigate(PlayerQueue)
                            expansionViewModel.collapse()
                        }
//                        CompactPlayerLayout(
//                            state = playerState,
//                            callbacks = playerCallbacks,
//                            onShowQueue = {
//                                navController.navigateToQueue()
//                                onCollapsePlayer()
//                            },
//                        )
                    }
                }
            }
        }
    }
}
