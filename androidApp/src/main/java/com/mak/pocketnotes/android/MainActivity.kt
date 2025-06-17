package com.mak.pocketnotes.android

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.mak.pocketnotes.android.common.navigation.PodcastNavHost
import com.mak.pocketnotes.android.common.navigation.PodcastNavigationWrapper
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.service.media.service.MediaPlayerService


class MainActivity : ComponentActivity() {

//    private val mediaViewModel by viewModel<MediaViewModel>()
    private var isServiceRunning = false
    private var mediaIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketNotesTheme {
                PodcastNavigationWrapper(
                    startService = { startMediaService() }
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