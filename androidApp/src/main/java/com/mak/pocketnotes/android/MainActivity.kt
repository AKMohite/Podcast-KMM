package com.mak.pocketnotes.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mak.pocketnotes.android.common.navigation.PodcastNav
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme
import com.mak.pocketnotes.service.media.service.MediaPlayerService


class MainActivity : ComponentActivity() {

//    private val mediaViewModel by viewModel<MediaViewModel>()
    private var isServiceRunning = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketNotesTheme {
                PodcastNav(
                    startService = {
                        startMediaService()
                    }
                )
            }
        }
    }

    private fun startMediaService() {
        if (!isServiceRunning) {
            val intent = Intent(this, MediaPlayerService::class.java)
            startForegroundService(intent)
        }
        isServiceRunning = true
    }
}
