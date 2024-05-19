package com.mak.pocketnotes.domain.models

import io.ktor.util.date.GMTDate

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

    fun readableDuration(): String {
        val hours = duration / 3600;
        val minutes = (duration % 3600) / 60;
        val seconds = duration % 60;
        var duration = ""
        if (hours > 0)
            duration += "$hours hrs "

        if (minutes > 0)
            duration += "$minutes min "

        if (seconds > 0)
            duration += "$seconds sec"

        return duration.trim()
    }

    fun asPlayableEpisode() = PlayableEpisode(
        id = id,
        title = title,
        speaker = "", // TODO is there a speaker
        image = image,
        track = audio,
        duration = duration
    )


}

fun List<PodcastEpisode>.asPlayableEpisodes(): List<PlayableEpisode> {
    return this.map {
        it.asPlayableEpisode()
    }
}