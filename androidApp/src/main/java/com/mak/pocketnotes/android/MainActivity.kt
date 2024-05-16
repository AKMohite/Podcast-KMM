package com.mak.pocketnotes.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mak.pocketnotes.android.common.navigation.PodcastNav
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketNotesTheme {
                PodcastNav()
            }
        }
    }
}
