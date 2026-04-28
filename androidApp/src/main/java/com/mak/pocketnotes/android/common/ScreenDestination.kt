package com.mak.pocketnotes.android.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.mak.pocketnotes.android.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal interface ScreenDestination : NavKey {
    @get:StringRes
    val title: Int
    companion object {
        fun isFullScreen(key: NavKey?): Boolean {
            return key is PodcastPlayer || key is PodcastDetail
        }
    }
}

internal interface BottomDestination : ScreenDestination {
    val icon: ImageVector
}

@Serializable
object Home : BottomDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.Home
    override val title: Int
        get() = R.string.bottom_discover
}

@Serializable
object Search : BottomDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.Search
    override val title: Int
        get() = R.string.bottom_search
}

@Serializable
object Subscribed : BottomDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.Star
    override val title: Int
        get() = R.string.bottom_subscribed
}

@Serializable
object Settings : BottomDestination {
    override val icon: ImageVector
        get() = Icons.Outlined.Settings
    override val title: Int
        get() = R.string.bottom_settings
}

@Serializable
data class PodcastDetail(@SerialName("podcast_id") val podcastId: String) : ScreenDestination {
    override val title: Int
        get() = R.string.podcast_details
}

@Serializable
object PodcastPlayer : ScreenDestination {
    override val title: Int
        get() = R.string.now_playing
}

internal val appDestinations = listOf(Home, Search, Subscribed, Settings)
