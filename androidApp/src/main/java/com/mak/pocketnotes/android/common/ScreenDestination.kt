package com.mak.pocketnotes.android.common

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

internal data class PodBottomItem(val title: String, val icon: ImageVector)

internal interface ScreenDestination {
    val title: String
    val route: String
    val routeWithArgs: String
}

object Home: ScreenDestination {
    override val title: String
        get() = "Podcast"

    override val route: String
        get() = "home-screen"

    override val routeWithArgs: String
        get() = route
}

object Search: ScreenDestination {
    override val title: String
        get() = "Search"
    override val route: String
        get() = "search-screen"
    override val routeWithArgs: String
        get() = route

}

object Subscribed: ScreenDestination {
    override val title: String
        get() = "Subscribed"
    override val route: String
        get() = "subscribed-screen"
    override val routeWithArgs: String
        get() = route

}

object Settings: ScreenDestination {
    override val title: String
        get() = "Settings"
    override val route: String
        get() = "settings-screen"
    override val routeWithArgs: String
        get() = route
}

object PodcastDetail: ScreenDestination {
    override val title: String
        get() = "Podcast details"
    override val route: String
        get() = "podcast-detail"
    override val routeWithArgs: String
        get() = "$route/{podcast_id}"

    val argument = listOf(
        navArgument(name = "podcast_id") { type = NavType.IntType }
    )
}

internal val appDestinations = listOf(Home, PodcastDetail)