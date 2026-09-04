package app.mak.pocketnotes.wearos

import android.app.Application
import android.os.StrictMode

class PocketNotesWearApp : Application() {
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
    super.onCreate()
  }
}
