package com.mak.pocketnotes.android.ai

import androidx.appfunctions.AppFunction
import androidx.appfunctions.AppFunctionElementNotFoundException
import androidx.appfunctions.AppFunctionSerializable
import androidx.appfunctions.AppFunctionService
import androidx.appfunctions.AppFunctionServiceEntryPoint
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.EpisodeRepository
import com.mak.pocketnotes.core.feature.domain.search.repository.SearchRepository
import com.mak.pocketnotes.media.PlayerController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

/**
 * Service entry point for podcast-related AppFunctions.
 */
@AppFunctionServiceEntryPoint(
  serviceName = "PodcastAppFunctionService",
  appFunctionXmlFileName = "podcast_app_function_service"
)
abstract class BasePodcastAppFunctionService : AppFunctionService() {

  private val searchRepository: SearchRepository by inject()
  private val episodeRepository: EpisodeRepository by inject()
  private val playerController: PlayerController by inject()

  /**
   * Search for podcasts by topic, title, or keyword.
   * Required workflow: Call this to obtain a valid "id" before calling "playLatestEpisode" or "playPodcastEpisode" if the ID is unknown.
   * @param query Search string for topic or name (e.g., "History", "Science").
   * @return List of podcasts matching the search query.
   */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun searchPodcasts(query: String): List<PodcastResponse> {
    val podcasts = searchRepository.searchPodcastsList(query).first()
    return podcasts.map {
      PodcastResponse(
        id = it.id,
        title = it.title,
        publisher = it.publisher,
        description = it.description,
        thumbnail = it.thumbnail
      )
    }
  }

  /**
   * Play a specific podcast episode by its unique identifier.
   * @param episodeId Unique identifier for the episode.
   * @throws AppFunctionElementNotFoundException If the episode ID is invalid or not found. Suggest searching for the podcast first.
   */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun playPodcastEpisode(episodeId: String) {
    val episode = episodeRepository.getEpisodeById(episodeId)
      ?: throw AppFunctionElementNotFoundException("Episode with ID $episodeId not found.")

    withContext(Dispatchers.Main) {
      playerController.playEpisode(episode)
    }
  }

  /**
   * Find and play the most recent episode of a podcast by its name.
   * @param podcastName Name of the podcast (e.g., "The Daily").
   * @throws AppFunctionElementNotFoundException If no podcast matches the name or no episodes are available.
   */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun playLatestEpisode(podcastName: String) {
    val podcasts = searchRepository.searchPodcastsList(podcastName).first()

    if (podcasts.isEmpty()) {
      throw AppFunctionElementNotFoundException("Podcast '$podcastName' not found.")
    }

    // Using the first podcast found
    val podcast = podcasts[0]
    val episodes = episodeRepository.observeEpisodes(
      com.mak.pocketnotes.core.feature.domain.home.models.EpisodeQueryParam(podcast.id)
    ).firstOrNull()

    if (episodes.isNullOrEmpty()) {
      throw AppFunctionElementNotFoundException("No episodes found for '$podcastName'.")
    }

    withContext(Dispatchers.Main) {
      playerController.playEpisode(episodes[0])
    }
  }

  /**
   * Add a specific podcast episode to the end of the current playback queue.
   * @param episodeId Unique identifier for the episode.
   * @throws AppFunctionElementNotFoundException If the episode ID is not found.
   */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun addToQueue(episodeId: String) {
    val episode = episodeRepository.getEpisodeById(episodeId)
      ?: throw AppFunctionElementNotFoundException("Episode with ID $episodeId not found.")

    withContext(Dispatchers.Main) {
      playerController.addToQueueEnd(episode)
    }
  }

  /**
   * Search for specific podcast episodes by title or topic.
   * @param query Search string for episode title or topic.
   * @return List of podcast episodes matching the search query.
   */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun searchEpisodes(query: String): List<EpisodeResponse> {
    val episodes = searchRepository.searchEpisodes(query).first()
    return episodes.map { it.toResponse() }
  }

  /**
   * Get the current playback state including the playing episode and progress.
   * @return Current playback state information.
   */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun getPlaybackState(): PlaybackStateResponse {
    val state = playerController.playerState.value
    return PlaybackStateResponse(
      isPlaying = state.isPlaying,
      currentEpisode = state.currentEpisode?.toResponse(),
      positionMs = state.positionMs,
      durationMs = state.durationMs,
      playbackSpeed = state.playbackSpeed
    )
  }

  /** Pause the current podcast playback. */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun pausePlayback() {
    withContext(Dispatchers.Main) {
      playerController.pause()
    }
  }

  /** Resume the current podcast playback. */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun resumePlayback() {
    withContext(Dispatchers.Main) {
      playerController.resume()
    }
  }

  /** Skip forward in the current episode (default 30 seconds). */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun skipForward() {
    withContext(Dispatchers.Main) {
      playerController.skipForward()
    }
  }

  /** Skip backward in the current episode (default 10 seconds). */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun skipBackward() {
    withContext(Dispatchers.Main) {
      playerController.skipBackward()
    }
  }

  /** Skip to the next episode in the playback queue. */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun skipToNext() {
    withContext(Dispatchers.Main) {
      playerController.skipToNext()
    }
  }

  /** Skip to the previous episode or restart the current one. */
  @AppFunction(isDescribedByKDoc = true)
  suspend fun skipToPrevious() {
    withContext(Dispatchers.Main) {
      playerController.skipToPrevious()
    }
  }

  private fun com.mak.pocketnotes.core.feature.domain.home.models.PodcastEpisode.toResponse() =
    EpisodeResponse(
      id = id,
      title = title,
      podcastId = podcastId,
      audioUrl = audio,
      durationSeconds = duration
    )
}

/** Represents a podcast entity returned from a search. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class PodcastResponse(
  /** Unique identifier of the podcast. */
  val id: String,
  /** Display title of the podcast. */
  val title: String,
  /** Name of the podcast publisher or network. */
  val publisher: String,
  /** Summary description of the podcast content. */
  val description: String,
  /** URL to the podcast's thumbnail image. */
  val thumbnail: String
)

/** Represents a podcast episode. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class EpisodeResponse(
  /** Unique identifier of the episode. */
  val id: String,
  /** Title of the episode. */
  val title: String,
  /** ID of the podcast this episode belongs to. */
  val podcastId: String,
  /** URL to the episode's audio stream. */
  val audioUrl: String,
  /** Duration of the episode in seconds. */
  val durationSeconds: Int
)

/** Represents the current playback status of the media player. */
@AppFunctionSerializable(isDescribedByKDoc = true)
data class PlaybackStateResponse(
  /** True if audio is currently playing. */
  val isPlaying: Boolean,
  /** The episode currently loaded in the player, if any. */
  val currentEpisode: EpisodeResponse?,
  /** Current playback position in milliseconds. */
  val positionMs: Long,
  /** Total duration of the current episode in milliseconds. */
  val durationMs: Long,
  /** Current playback speed (e.g., 1.0, 1.5). */
  val playbackSpeed: Float
)
