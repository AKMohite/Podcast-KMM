package com.mak.pocketnotes.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class EpisodeDTO(
    @SerialName("audio")
    val audio: String? = null,
    @SerialName("audio_length_sec")
    val audioLengthSec: Int? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("explicit_content")
    val explicitContent: Boolean? = null,
    @SerialName("guid_from_rss")
    val guidFromRss: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("link")
    val link: String? = null,
    @SerialName("listennotes_edit_url")
    val listennotesEditUrl: String? = null,
    @SerialName("listennotes_url")
    val listennotesUrl: String? = null,
    @SerialName("maybe_audio_invalid")
    val maybeAudioInvalid: Boolean? = null,
    @SerialName("pub_date_ms")
    val pubDateMs: Long? = null,
    @SerialName("thumbnail")
    val thumbnail: String? = null,
    @SerialName("title")
    val title: String? = null
)