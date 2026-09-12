package com.mak.pocketnotes.core.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import com.mak.pocketnotes.core.common.WidgetUpdater
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal class GlanceWidgetUpdater(private val context: Context) : WidgetUpdater {
  override fun updateSubscriptionWidget() {
    MainScope().launch {
      val widget = SubscriptionGridWidget()
      widget.updateAll(context)

      // Update preview for Android 15+
      if (android.os.Build.VERSION.SDK_INT >= 35) {
        val manager = GlanceAppWidgetManager(context)
        manager.setWidgetPreviews(SubscriptionGridWidgetReceiver::class)
      }
    }
  }
}
