package com.mak.pocketnotes.core.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.mak.pocketnotes.core.database.dao.SubscriptionDAO
import com.mak.pocketnotes.core.database.queries.Podcasts
import com.mak.pocketnotes.core.widget.ui.PocketGlanceTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SubscriptionGridWidget : GlanceAppWidget(), KoinComponent {

  private val subscriptionDAO: SubscriptionDAO by inject()

  companion object {
    private val SMALL_SQUARE = DpSize(60.dp, 60.dp)
    private val MEDIUM_RECTANGLE = DpSize(120.dp, 120.dp)
    private val LARGE_RECTANGLE = DpSize(240.dp, 120.dp)
  }

  override val sizeMode = SizeMode.Responsive(
    setOf(SMALL_SQUARE, MEDIUM_RECTANGLE, LARGE_RECTANGLE)
  )

  override val previewSizeMode = SizeMode.Responsive(
    setOf(SMALL_SQUARE, MEDIUM_RECTANGLE, LARGE_RECTANGLE)
  )

  override suspend fun provideGlance(context: Context, id: GlanceId) {
    provideContent {
      PocketGlanceTheme {
        val subscriptions by subscriptionDAO.getSubscribedPodcasts().collectAsState(emptyList())
        val size = LocalSize.current
        SubscriptionGridContent(subscriptions, size)
      }
    }
  }

  override suspend fun providePreview(context: Context, widgetCategory: Int) {
    provideContent {
      PocketGlanceTheme {
        SubscriptionGridContent(emptyList(), MEDIUM_RECTANGLE)
      }
    }
  }

  @Composable
  private fun SubscriptionGridContent(subscriptions: List<Podcasts>, size: DpSize) {
    val columnCount = when {
      size.width >= LARGE_RECTANGLE.width -> 4
      size.width >= MEDIUM_RECTANGLE.width -> 2
      else -> 1
    }

    Column(
      modifier = GlanceModifier
        .fillMaxSize()
        .background(GlanceTheme.colors.background)
        .padding(8.dp)
    ) {
      if (size.height >= MEDIUM_RECTANGLE.height) {
        Text(
          text = "My Subscriptions",
          style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = GlanceTheme.colors.onSurface
          ),
          modifier = GlanceModifier.padding(bottom = 8.dp)
        )
      }

      if (subscriptions.isEmpty()) {
        Box(
          modifier = GlanceModifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No subscriptions yet",
            style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant)
          )
        }
      } else {
        val rowCount = if (size.height >= LARGE_RECTANGLE.height) 2 else 1
        val displayCount = columnCount * rowCount
        val displayList = subscriptions.take(displayCount)

        Column {
          for (i in 0 until (displayList.size + columnCount - 1) / columnCount) {
            Row(modifier = GlanceModifier.fillMaxWidth().padding(bottom = 4.dp)) {
              for (j in 0 until columnCount) {
                val index = i * columnCount + j
                if (index < displayList.size) {
                  PodcastItem(displayList[index], modifier = GlanceModifier.defaultWeight())
                } else {
                  Spacer(modifier = GlanceModifier.defaultWeight())
                }
                if (j < columnCount - 1) {
                  Spacer(modifier = GlanceModifier.size(4.dp))
                }
              }
            }
          }
        }
      }
    }
  }

  @Composable
  private fun PodcastItem(podcast: Podcasts, modifier: GlanceModifier) {
    val intent = Intent(Intent.ACTION_VIEW, "pocketnotes://podcast/${podcast.id}".toUri()).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    Column(
      modifier = modifier
        .clickable(actionStartActivity(intent))
        .padding(4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = GlanceModifier
          .size(48.dp)
          .background(GlanceTheme.colors.secondaryContainer)
      ) {
        // Thumbnail placeholder
      }
      Spacer(modifier = GlanceModifier.size(4.dp))
      Text(
        text = podcast.title,
        style = TextStyle(fontSize = 10.sp, color = GlanceTheme.colors.onSurface),
        maxLines = 1
      )
    }
  }
}
