package com.mak.pocketnotes.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ExtraDTO(
    @SerialName("amazon_music_url")
    val amazonMusicUrl: String,
    @SerialName("facebook_handle")
    val facebookHandle: String,
    @SerialName("google_url")
    val googleUrl: String,
    @SerialName("instagram_handle")
    val instagramHandle: String,
    @SerialName("linkedin_url")
    val linkedinUrl: String,
    @SerialName("patreon_handle")
    val patreonHandle: String,
    @SerialName("spotify_url")
    val spotifyUrl: String,
    @SerialName("twitter_handle")
    val twitterHandle: String,
    @SerialName("url1")
    val url1: String,
    @SerialName("url2")
    val url2: String,
    @SerialName("url3")
    val url3: String,
    @SerialName("wechat_handle")
    val wechatHandle: String,
    @SerialName("youtube_url")
    val youtubeUrl: String
)