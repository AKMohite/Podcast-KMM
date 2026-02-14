package com.mak.pocketnotes.android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.mak.pocketnotes.android.common.navigation.PodcastNavigationWrapper
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.service.media.service.MediaPlayerService


class MainActivity : ComponentActivity() {

//    private val mediaViewModel by viewModel<MediaViewModel>()
    private var isServiceRunning = false
    private var mediaIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.Transparent.toArgb(), // Color for light theme
                darkScrim = Color.Transparent.toArgb()  // Color for dark theme
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.Transparent.toArgb(), // Color for light theme
                darkScrim = Color.Transparent.toArgb()  // Color for dark theme
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            PocketNotesTheme {
                val isSystemDark = isSystemInDarkTheme()
                val statusBarColor = if (isSystemDark) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                PodcastNavigationWrapper(
                    startService = { startMediaService() },
                    modifier = Modifier
                        .safeDrawingPadding()
                )
            }
        }
    }

    override fun onDestroy() {
        mediaIntent?.let { stopService(it) }
        isServiceRunning = false
        mediaIntent = null
        super.onDestroy()
    }

    private fun startMediaService() {
        if (!isServiceRunning) {
            mediaIntent = Intent(this, MediaPlayerService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(mediaIntent)
            } else {
                startService(mediaIntent)
            }
        }
        isServiceRunning = true
    }
}