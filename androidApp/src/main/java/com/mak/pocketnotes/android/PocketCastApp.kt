package com.mak.pocketnotes.android

import android.app.Application
import android.os.StrictMode
import androidx.compose.ui.ComposeUiFlags

internal class PocketCastApp : Application() {
  override fun onCreate() {
    if (BuildConfig.DEBUG) {
      StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .build()
      )
      StrictMode.setVmPolicy(
        StrictMode.VmPolicy.Builder()
          .detectAll()
          .penaltyLog()
          .build()
      )
    }
    ComposeUiFlags.isMediaQueryIntegrationEnabled = true
    super.onCreate()
  }
}
