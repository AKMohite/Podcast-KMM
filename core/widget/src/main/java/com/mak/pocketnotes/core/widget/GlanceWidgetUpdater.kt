package com.mak.pocketnotes.core.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import com.mak.pocketnotes.core.common.WidgetUpdater
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal class GlanceWidgetUpdater(context: Context) : WidgetUpdater {

  private val appContext = context.applicationContext

  override suspend fun updateSubscriptionWidget() {
    val widget = SubscriptionGridWidget()
    widget.updateAll(appContext)

    // Update preview for Android 15+
    if (android.os.Build.VERSION.SDK_INT >= 35) {
      val manager = GlanceAppWidgetManager(appContext)
      manager.setWidgetPreviews(SubscriptionGridWidgetReceiver::class)
    }
  }
}
