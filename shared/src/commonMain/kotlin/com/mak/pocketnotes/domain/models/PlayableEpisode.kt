package com.mak.pocketnotes.domain.models

data class PlayableEpisode(
    val id: String,
    val title: String,
    val speaker: String,
    val image: String,
    val track: String, // audio url
    val duration: Int
) {
    companion object {
        val EMPTY = PlayableEpisode(
            "",
            "",
            "",
            "",
            "",
            0
        )
    }
}
