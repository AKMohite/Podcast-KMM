package com.mak.pocketnotes.android

import android.app.Application
import androidx.compose.ui.ComposeUiFlags

internal class PocketCastApp : Application() {
  override fun onCreate() {
    ComposeUiFlags.isMediaQueryIntegrationEnabled = true
    super.onCreate()
  }
}
