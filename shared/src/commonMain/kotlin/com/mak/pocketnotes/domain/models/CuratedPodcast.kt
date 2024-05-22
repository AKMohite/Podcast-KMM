package com.mak.pocketnotes.domain.models

data class CuratedPodcast(
    val id: String,
    val title: String,
    val podcasts: List<SectionPodcast>
) {
    fun getCuratedPodcasts(noOfRows: Int = 2): List<List<SectionPodcast>> {
        return podcasts.chunked(noOfRows)
    }
}

data class SectionPodcast(
    val id: String,
    val title: String,
//    val description: String,
    val image: String
)
