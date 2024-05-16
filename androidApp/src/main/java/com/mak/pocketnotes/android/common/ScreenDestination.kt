package com.mak.pocketnotes.android.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.mak.pocketnotes.android.R

internal interface ScreenDestination {
    @get:StringRes
    val title: Int
    val route: String
    val routeWithArgs: String
}

internal interface BottomDestination: ScreenDestination {
    val icon: ImageVector
}

object Home: BottomDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.Home
    @get:StringRes
    override val title: Int
        get() = R.string.bottom_discover

    override val route: String
        get() = "home-screen"

    override val routeWithArgs: String
        get() = route
}

object Search: BottomDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.Search

    @get:StringRes
    override val title: Int
        get() = R.string.bottom_search
    override val route: String
        get() = "search-screen"
    override val routeWithArgs: String
        get() = route

}

object Subscribed: BottomDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.Star

    @get:StringRes
    override val title: Int
        get() = R.string.bottom_subscribed
    override val route: String
        get() = "subscribed-screen"
    override val routeWithArgs: String
        get() = route

}

object Settings: BottomDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.Settings
    @get:StringRes
    override val title: Int
        get() = R.string.bottom_settings
    override val route: String
        get() = "settings-screen"
    override val routeWithArgs: String
        get() = route
}

object PodcastDetail: ScreenDestination {
    @get:StringRes
    override val title: Int
        get() = R.string.podcast_details
    override val route: String
        get() = "podcast-detail"
    override val routeWithArgs: String
        get() = "$route/{podcast_id}"

    val argument = listOf(
        navArgument(name = "podcast_id") { type = NavType.IntType }
    )
}

internal val appDestinations = listOf(Home, PodcastDetail)