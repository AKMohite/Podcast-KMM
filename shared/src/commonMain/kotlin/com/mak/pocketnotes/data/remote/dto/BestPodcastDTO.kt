package com.mak.pocketnotes.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BestPodcastDTO(
    @SerialName("has_next")
    val hasNext: Boolean? = null,
    @SerialName("has_previous")
    val hasPrevious: Boolean? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("listennotes_url")
    val listennotesUrl: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("next_page_number")
    val nextPageNumber: Int? = null,
    @SerialName("page_number")
    val pageNumber: Int? = null,
    @SerialName("parent_id")
    val parentId: Int? = null,
    @SerialName("podcasts")
    val podcasts: List<PodcastDTO>? = null,
    @SerialName("previous_page_number")
    val previousPageNumber: Int? = null,
    @SerialName("total")
    val total: Int? = null
)
@Serializable
internal data class PodcastDTO(
    @SerialName("audio_length_sec")
    val audioLengthSec: Int? = null,
    @SerialName("country")
    val country: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("earliest_pub_date_ms")
    val earliestPubDateMs: Long? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("explicit_content")
    val explicitContent: Boolean? = null,
    @SerialName("extra")
    val extra: ExtraDTO? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("is_claimed")
    val isClaimed: Boolean? = null,
    @SerialName("itunes_id")
    val itunesId: Int? = null,
    @SerialName("language")
    val language: String? = null,
    @SerialName("latest_episode_id")
    val latestEpisodeId: String? = null,
    @SerialName("latest_pub_date_ms")
    val latestPubDateMs: Long? = null,
    @SerialName("listen_score")
    val listenScore: Int? = null, // for pro version it will return int else it will return string
    @SerialName("listen_score_global_rank")
    val listenScoreGlobalRank: String? = null,
    @SerialName("listennotes_url")
    val listennotesUrl: String? = null,
    @SerialName("looking_for")
    val lookingFor: LookingForDTO? = null,
    @SerialName("publisher")
    val publisher: String? = null,
    @SerialName("rss")
    val rss: String? = null,
    @SerialName("thumbnail")
    val thumbnail: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("total_episodes")
    val totalEpisodes: Int? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("update_frequency_hours")
    val updateFrequencyHours: Int? = null,
    @SerialName("website")
    val website: String? = null,
    @SerialName("episodes")
    val episodes: List<EpisodeDTO>? = null
)
@Serializable
internal data class ExtraDTO(
    @SerialName("amazon_music_url")
    val amazonMusicUrl: String? = null,
    @SerialName("facebook_handle")
    val facebookHandle: String? = null,
    @SerialName("google_url")
    val googleUrl: String? = null,
    @SerialName("instagram_handle")
    val instagramHandle: String? = null,
    @SerialName("linkedin_url")
    val linkedinUrl: String? = null,
    @SerialName("patreon_handle")
    val patreonHandle: String? = null,
    @SerialName("spotify_url")
    val spotifyUrl: String? = null,
    @SerialName("twitter_handle")
    val twitterHandle: String? = null,
    @SerialName("url1")
    val url1: String? = null,
    @SerialName("url2")
    val url2: String? = null,
    @SerialName("url3")
    val url3: String? = null,
    @SerialName("wechat_handle")
    val wechatHandle: String? = null,
    @SerialName("youtube_url")
    val youtubeUrl: String? = null
)

@Serializable
internal data class LookingForDTO(
    @SerialName("cohosts")
    val cohosts: Boolean? = null,
    @SerialName("cross_promotion")
    val crossPromotion: Boolean? = null,
    @SerialName("guests")
    val guests: Boolean? = null,
    @SerialName("sponsors")
    val sponsors: Boolean? = null
)
