package com.mak.pocketnotes.android.common.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.mak.pocketnotes.android.common.Home
import com.mak.pocketnotes.android.common.PodcastDetail
import com.mak.pocketnotes.android.common.ScreenDestination
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import com.mak.pocketnotes.android.common.viewmodel.MediaViewModel
import com.mak.pocketnotes.android.feature.home.HomeScreen
import com.mak.pocketnotes.android.feature.home.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PodcastNavigation(
    startService: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mediaViewModel: MediaViewModel = koinViewModel()
    val navController = rememberNavController()
    val adaptiveInfo = currentWindowAdaptiveInfoV2()

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

//    TODO need to manage all screen navigation with nav3
    val homeViewModel: HomeViewModel = koinViewModel()
    val state by homeViewModel.uiState.collectAsState()
    HomeScreen(
        gotoDetails = { podcastId ->
            navController.navigate(
                "${PodcastDetail.route}/$podcastId"
            )
        },
        state = state,
        loadNextPodcasts = homeViewModel::loadPodcasts
    )
}