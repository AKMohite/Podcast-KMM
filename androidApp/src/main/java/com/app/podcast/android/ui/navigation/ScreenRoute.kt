package com.app.podcast.android.ui.navigation

sealed class ScreenRoute(val route: String) {
    object PodcastList: ScreenRoute("podcastList")
}
