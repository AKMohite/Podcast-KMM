package com.mak.pocketnotes.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class EpisodeDTO(
    @SerialName("audio")
    val audio: String?,
    @SerialName("audio_length_sec")
    val audioLengthSec: Int?,
    @SerialName("description")
    val description: String?,
    @SerialName("explicit_content")
    val explicitContent: Boolean?,
    @SerialName("guid_from_rss")
    val guidFromRss: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("image")
    val image: String?,
    @SerialName("link")
    val link: String?,
    @SerialName("listennotes_edit_url")
    val listennotesEditUrl: String?,
    @SerialName("listennotes_url")
    val listennotesUrl: String?,
    @SerialName("maybe_audio_invalid")
    val maybeAudioInvalid: Boolean?,
    @SerialName("pub_date_ms")
    val pubDateMs: Long?,
    @SerialName("thumbnail")
    val thumbnail: String?,
    @SerialName("title")
    val title: String?
)