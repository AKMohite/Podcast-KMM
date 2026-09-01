package com.mak.pocketnotes.android

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import com.mak.pocketnotes.android.common.Discover
import com.mak.pocketnotes.android.common.PlayerQueue
import com.mak.pocketnotes.android.common.PodcastDetail
import com.mak.pocketnotes.android.common.Search
import com.mak.pocketnotes.android.common.Settings
import com.mak.pocketnotes.android.common.Subscribed
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

internal class AppMainViewmodel : ViewModel() {

  private val _navEvents = Channel<NavEvent>(Channel.BUFFERED)
  val navEvents = _navEvents.receiveAsFlow()

  fun handleIntent(intent: Intent) {
    val action = intent.action
    val data = intent.data

    when {
      // 1. Handle Custom Actions (Notifications, Media Controls)
      action == "com.podcast.app.action.SHOW_PLAYER" -> {
        _navEvents.trySend(NavEvent.ShowPlayer)
      }

      action == "com.podcast.app.action.SHOW_QUEUE" -> {
        _navEvents.trySend(NavEvent.ShowQueue)
      }

      action == "com.podcast.app.action.SHOW_EPISODE" -> {
        val episodeId = intent.getStringExtra("episode_id")
        if (episodeId != null) {
          _navEvents.trySend(NavEvent.ShowEpisode(episodeId))
        }
      }

      // 2. Handle App Links / Deep Links
      action == Intent.ACTION_VIEW && data != null -> {
        val route = parseUri(data)

        route?.let {
          _navEvents.trySend(NavEvent.Navigate(it))
        }
      }
    }
  }

  private fun parseUri(uri: Uri): NavKey? {
    val host = uri.host ?: ""
    val path = uri.path ?: ""

    // Normalize path/host depending on scheme
    return if (uri.scheme == "pocketnotes") {
      // Handle pocketnotes://host/path
      when (host) {
        "podcast" -> {
          val id = path.substringAfter("/", "").substringBefore("/")
          if (id.isNotEmpty()) PodcastDetail(id) else Discover
        }

        "search" -> Search(query = uri.getQueryParameter("q"))
        "subscribed" -> Subscribed
        "settings" -> Settings
        "player" -> {
          _navEvents.trySend(NavEvent.ShowPlayer)
          null
        }

        "queue" -> PlayerQueue
        "discover" -> Discover
        else -> Discover
      }
    } else {
      // Handle https://pocketnotes.mak.com/path
      when {
        path.startsWith("/podcast/") -> {
          val id = path.substringAfter("/podcast/").substringBefore("/")
          if (id.isNotEmpty()) PodcastDetail(id) else Discover
        }

        path.startsWith("/search") -> Search(query = uri.getQueryParameter("q"))
        path.startsWith("/subscribed") -> Subscribed
        path.startsWith("/settings") -> Settings
        path.startsWith("/player") -> {
          _navEvents.trySend(NavEvent.ShowPlayer)
          null
        }

        path.startsWith("/queue") -> PlayerQueue
        path.startsWith("/discover") || path == "/" || path.isEmpty() -> Discover
        else -> Discover
      }
    }
  }
}

sealed interface NavEvent {
  /** Expand the player pane/overlay to full. */
  data object ShowPlayer : NavEvent

  /** Navigate to a specific episode detail AND start playback. */
  data class ShowEpisode(
    val episodeId: String
  ) : NavEvent

  /** Navigate to the queue screen (Compact) or reveal queue pane (Expanded+). */
  data object ShowQueue : NavEvent

  /** Generic navigation to a screen. */
  data class Navigate(val route: NavKey) : NavEvent
}
