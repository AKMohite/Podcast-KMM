package com.mak.pocketnotes.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BestPodcastDTO(
    @SerialName("has_next")
    val hasNext: Boolean?,
    @SerialName("has_previous")
    val hasPrevious: Boolean?,
    @SerialName("id")
    val id: Int?,
    @SerialName("listennotes_url")
    val listennotesUrl: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("next_page_number")
    val nextPageNumber: Int?,
    @SerialName("page_number")
    val pageNumber: Int?,
    @SerialName("parent_id")
    val parentId: Int?,
    @SerialName("podcasts")
    val podcasts: List<PodcastDTO>?,
    @SerialName("previous_page_number")
    val previousPageNumber: Int?,
    @SerialName("total")
    val total: Int?
)
@Serializable
internal data class PodcastDTO(
    @SerialName("audio_length_sec")
    val audioLengthSec: Int?,
    @SerialName("country")
    val country: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("earliest_pub_date_ms")
    val earliestPubDateMs: Long?,
    @SerialName("email")
    val email: String?,
    @SerialName("explicit_content")
    val explicitContent: Boolean?,
    @SerialName("extra")
    val extra: ExtraDTO?,
    @SerialName("genre_ids")
    val genreIds: List<Int?>?,
    @SerialName("id")
    val id: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("is_claimed")
    val isClaimed: Boolean?,
    @SerialName("itunes_id")
    val itunesId: Int?,
    @SerialName("language")
    val language: String?,
    @SerialName("latest_episode_id")
    val latestEpisodeId: String?,
    @SerialName("latest_pub_date_ms")
    val latestPubDateMs: Long?,
    @SerialName("listen_score")
    val listenScore: Int?, // for pro version it will return int else it will return string
    @SerialName("listen_score_global_rank")
    val listenScoreGlobalRank: String?,
    @SerialName("listennotes_url")
    val listennotesUrl: String?,
    @SerialName("looking_for")
    val lookingFor: LookingForDTO?,
    @SerialName("publisher")
    val publisher: String?,
    @SerialName("rss")
    val rss: String?,
    @SerialName("thumbnail")
    val thumbnail: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("total_episodes")
    val totalEpisodes: Int?,
    @SerialName("type")
    val type: String?,
    @SerialName("update_frequency_hours")
    val updateFrequencyHours: Int?,
    @SerialName("website")
    val website: String?,
    @SerialName("episodes")
    val episodes: List<EpisodeDTO>? = emptyList()
)
@Serializable
internal data class ExtraDTO(
    @SerialName("amazon_music_url")
    val amazonMusicUrl: String?,
    @SerialName("facebook_handle")
    val facebookHandle: String?,
    @SerialName("google_url")
    val googleUrl: String?,
    @SerialName("instagram_handle")
    val instagramHandle: String?,
    @SerialName("linkedin_url")
    val linkedinUrl: String?,
    @SerialName("patreon_handle")
    val patreonHandle: String?,
    @SerialName("spotify_url")
    val spotifyUrl: String?,
    @SerialName("twitter_handle")
    val twitterHandle: String?,
    @SerialName("url1")
    val url1: String?,
    @SerialName("url2")
    val url2: String?,
    @SerialName("url3")
    val url3: String?,
    @SerialName("wechat_handle")
    val wechatHandle: String?,
    @SerialName("youtube_url")
    val youtubeUrl: String?
)

@Serializable
internal data class LookingForDTO(
    @SerialName("cohosts")
    val cohosts: Boolean?,
    @SerialName("cross_promotion")
    val crossPromotion: Boolean?,
    @SerialName("guests")
    val guests: Boolean?,
    @SerialName("sponsors")
    val sponsors: Boolean?
)
