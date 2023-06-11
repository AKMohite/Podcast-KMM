package com.mak.pocketnotes.domain.models

import io.ktor.util.date.GMTDate

@kotlinx.serialization.Serializable
data class Podcast(
    val id: String,
    val description: String,
    val image: String,
    val listenScore: Int,
    val publisher: String,
    val thumbnail: String,
    val title: String,
    val totalEpisodes: Int,
    val type: String,
    val website: String,
    val episodes: List<PodcastEpisode> = emptyList()
)

@kotlinx.serialization.Serializable
data class PodcastEpisode(
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val listennotesUrl: String,
    val thumbnail: String,
    val uploadedAt: Long,
    val audio: String,
    val duration: Int // in seconds
) {
    fun readableTime(): String {
        if (uploadedAt <= 0) return ""
        val gmtDate = GMTDate(uploadedAt)
        return "${gmtDate.month.value} ${gmtDate.dayOfMonth}"
    }
}
