package com.mak.pocketnotes.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mak.pocketnotes.android.feature.home.HomeScreen
import com.mak.pocketnotes.android.ui.theme.PocketNotesTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketNotesTheme {
                HomeScreen()
            }
        }
    }
}

