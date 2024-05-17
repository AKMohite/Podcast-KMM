package com.mak.pocketnotes.android.common.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import com.mak.pocketnotes.android.common.appDestinations
import com.mak.pocketnotes.android.common.ui.MiniPlayer
import com.mak.pocketnotes.android.feature.home.HomeScreen
import com.mak.pocketnotes.android.feature.podcastdetail.PodcastDetailScreen
import com.mak.pocketnotes.utils.sample.sampleEpisodes

@Composable
internal fun PodcastNav() {
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
    val currentScreen = appDestinations.find {
        backStackEntry?.destination?.route == it.route || backStackEntry?.destination?.route == it.routeWithArgs
    } ?: Home

    val bottomBarItems = listOf(
        Home,
        Search,
        Subscribed,
        Settings
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Column {
//                TODO handle player visibility
//                AnimatedVisibility(visible = true) {
                    MiniPlayer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .clickable {  },
                        episode = sampleEpisodes[0],
                        play = {},
                        next = {}
                    )
//                }
                PodBottomNavigation(
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
                    movieId = movieId
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