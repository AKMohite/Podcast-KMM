package com.mak.pocketnotes.core.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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
import androidx.glance.unit.ColorProvider
import com.mak.pocketnotes.core.widget.ui.PocketGlanceTheme

class PodcastWidget : GlanceAppWidget() {

  companion object {
    private val SMALL_SQUARE = DpSize(60.dp, 60.dp)
    private val HORIZONTAL_RECTANGLE = DpSize(200.dp, 60.dp)
    private val LARGE_RECTANGLE = DpSize(200.dp, 120.dp)
  }

  override val sizeMode = SizeMode.Responsive(
    setOf(SMALL_SQUARE, HORIZONTAL_RECTANGLE, LARGE_RECTANGLE)
  )

  override suspend fun provideGlance(context: Context, id: GlanceId) {
    provideContent {
      PocketGlanceTheme {
        val size = LocalSize.current
        PodcastWidgetContent(size)
      }
    }
  }

  override suspend fun providePreview(context: Context, widgetCategory: Int) {
    super.providePreview(context, widgetCategory)
    provideContent {
      PocketGlanceTheme {
        val size = LocalSize.current
        PodcastWidgetContent(size)
      }
    }
//    val manager = GlanceAppWidgetManager(context)
//    manager.setWidgetPreviews()
  }

  @Composable
  private fun PodcastWidgetContent(size: DpSize) {
    val playerIntent = Intent(Intent.ACTION_VIEW, "pocketnotes://player".toUri()).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    val boxModifier = GlanceModifier
      .fillMaxSize()
      .background(GlanceTheme.colors.background)
      .padding(8.dp)
      .clickable(actionStartActivity(playerIntent))

    when {
      size.height >= LARGE_RECTANGLE.height -> {
        LargeLayout(boxModifier, playerIntent)
      }

      size.width >= HORIZONTAL_RECTANGLE.width -> {
        MediumLayout(boxModifier, playerIntent)
      }

      else -> {
        SmallLayout(boxModifier, playerIntent)
      }
    }
  }

  @Composable
  private fun SmallLayout(modifier: GlanceModifier, playerIntent: Intent) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
      PlayButton(playerIntent)
    }
  }

  @Composable
  private fun MediumLayout(modifier: GlanceModifier, playerIntent: Intent) {
    Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically
    ) {
      ArtworkPlaceholder(48.dp)
      Spacer(modifier = GlanceModifier.size(8.dp))
      EpisodeInfo(modifier = GlanceModifier.defaultWeight())
      Spacer(modifier = GlanceModifier.size(8.dp))
      PlayButton(playerIntent)
    }
  }

  @Composable
  private fun LargeLayout(modifier: GlanceModifier, playerIntent: Intent) {
    Column(modifier = modifier) {
      Row(
        modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        ArtworkPlaceholder(64.dp)
        Spacer(modifier = GlanceModifier.size(12.dp))
        EpisodeInfo(modifier = GlanceModifier.defaultWeight())
      }
      Spacer(modifier = GlanceModifier.size(8.dp))
      Row(
        modifier = GlanceModifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
      ) {
        ControlButton("⟲", playerIntent)
        Spacer(modifier = GlanceModifier.size(24.dp))
        PlayButton(playerIntent)
        Spacer(modifier = GlanceModifier.size(24.dp))
        ControlButton("⟳", playerIntent)
      }
    }
  }

  @Composable
  private fun ArtworkPlaceholder(size: androidx.compose.ui.unit.Dp) {
    Box(
      modifier = GlanceModifier
        .size(size)
        .background(ColorProvider(Color.Gray))
    ) {}
  }

  @Composable
  private fun EpisodeInfo(modifier: GlanceModifier) {
    Column(modifier = modifier) {
      Text(
        text = "Episode Title",
        style = TextStyle(
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = GlanceTheme.colors.onSurface
        ),
        maxLines = 1
      )
      Text(
        text = "Podcast Publisher",
        style = TextStyle(
          fontSize = 14.sp,
          color = GlanceTheme.colors.onSurfaceVariant
        ),
        maxLines = 1
      )
    }
  }

  @Composable
  private fun PlayButton(playerIntent: Intent) {
    Box(
      modifier = GlanceModifier
        .size(48.dp)
        .background(GlanceTheme.colors.primaryContainer)
        .clickable(actionStartActivity(playerIntent)),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "▶",
        style = TextStyle(fontSize = 20.sp, color = GlanceTheme.colors.onPrimaryContainer)
      )
    }
  }

  @Composable
  private fun ControlButton(text: String, playerIntent: Intent) {
    Box(
      modifier = GlanceModifier
        .size(40.dp)
        .clickable(actionStartActivity(playerIntent)),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = text,
        style = TextStyle(fontSize = 18.sp, color = GlanceTheme.colors.onSurface)
      )
    }
  }
}
