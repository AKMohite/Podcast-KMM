package com.mak.pocketnotes.core.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPodcastResultDTO(
  @SerialName("audio_length_sec")
  val audioLengthSec: Int? = null,
  @SerialName("description_highlighted")
  val descriptionHighlighted: String? = null,
  @SerialName("description_original")
  val descriptionOriginal: String? = null,
  @SerialName("earliest_pub_date_ms")
  val earliestPubDateMs: Long? = null,
  @SerialName("email")
  val email: String? = null,
  @SerialName("explicit_content")
  val explicitContent: Boolean? = null,
  @SerialName("genre_ids")
  val genreIds: List<Int?>? = null,
  @SerialName("has_guest_interviews")
  val hasGuestInterviews: Boolean? = null,
  @SerialName("has_sponsors")
  val hasSponsors: Boolean? = null,
  @SerialName("id")
  val id: String? = null,
  @SerialName("image")
  val image: String? = null,
  @SerialName("itunes_id")
  val itunesId: Int? = null,
  @SerialName("latest_episode_id")
  val latestEpisodeId: String? = null,
  @SerialName("latest_pub_date_ms")
  val latestPubDateMs: Long? = null,
  @SerialName("listen_score")
  val listenScore: String? = null,
  @SerialName("listen_score_global_rank")
  val listenScoreGlobalRank: String? = null,
  @SerialName("listennotes_url")
  val listennotesUrl: String? = null,
  @SerialName("publisher_highlighted")
  val publisherHighlighted: String? = null,
  @SerialName("publisher_original")
  val publisherOriginal: String? = null,
  @SerialName("rss")
  val rss: String? = null,
  @SerialName("thumbnail")
  val thumbnail: String? = null,
  @SerialName("title_highlighted")
  val titleHighlighted: String? = null,
  @SerialName("title_original")
  val titleOriginal: String? = null,
  @SerialName("total_episodes")
  val totalEpisodes: Int? = null,
  @SerialName("update_frequency_hours")
  val updateFrequencyHours: Int? = null,
  @SerialName("website")
  val website: String? = null
)
