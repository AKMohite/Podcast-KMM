package com.app.podcast.android.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.podcast.android.ui.home.PodcastListScreen
import com.app.podcast.android.ui.navigation.ScreenRoute
import com.app.podcast.android.ui.theme.PodcastTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PodcastTheme{
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ScreenRoute.PodcastList.route
                    ) {
                        composable(
                            route = ScreenRoute.PodcastList.route
                        ) {
                            PodcastListScreen(navController)
                        }
                    }
                }
            }
        }
    }
}
