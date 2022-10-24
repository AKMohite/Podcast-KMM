package com.mak.pocketnotes.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PodcastDTO(
    @SerialName("audio_length_sec")
    val audioLengthSec: Int,
    @SerialName("country")
    val country: String,
    @SerialName("description")
    val description: String,
    @SerialName("earliest_pub_date_ms")
    val earliestPubDateMs: Long,
    @SerialName("email")
    val email: String,
    @SerialName("explicit_content")
    val explicitContent: Boolean,
    @SerialName("extra")
    val extra: ExtraDTO,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    @SerialName("id")
    val id: String,
    @SerialName("image")
    val image: String,
    @SerialName("is_claimed")
    val isClaimed: Boolean,
    @SerialName("itunes_id")
    val itunesId: Int,
    @SerialName("language")
    val language: String,
    @SerialName("latest_episode_id")
    val latestEpisodeId: String,
    @SerialName("latest_pub_date_ms")
    val latestPubDateMs: Long,
    @SerialName("listen_score")
    val listenScore: Int,
    @SerialName("listen_score_global_rank")
    val listenScoreGlobalRank: String,
    @SerialName("listennotes_url")
    val listennotesUrl: String,
    @SerialName("looking_for")
    val lookingFor: LookingForDTO,
    @SerialName("publisher")
    val publisher: String,
    @SerialName("rss")
    val rss: String,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("title")
    val title: String,
    @SerialName("total_episodes")
    val totalEpisodes: Int,
    @SerialName("type")
    val type: String,
    @SerialName("update_frequency_hours")
    val updateFrequencyHours: Int,
    @SerialName("website")
    val website: String
)