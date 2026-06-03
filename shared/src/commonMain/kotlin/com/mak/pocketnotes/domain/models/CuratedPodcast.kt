package com.mak.pocketnotes.domain.models

data class CuratedPodcast(
    val id: String,
    val title: String,
    val description: String,
    val podcasts: List<SectionPodcast>
) {
    fun getCuratedPodcasts(noOfRows: Int = 2): List<List<SectionPodcast>> {
        return podcasts.chunked(noOfRows)
    }

    /**
     * these images are only for expanded screens to show in curated section header
     */
    fun getSectionImages(targetSize: Int = 9): List<String> {
        if (podcasts.isEmpty()) return emptyList()
        return List(targetSize) { index ->
            podcasts[index % podcasts.size].thumbnail
        }
    }
}

data class SectionPodcast(
    val id: String,
    val title: String,
//    val description: String,
    val image: String,
    val thumbnail: String,
    val publisher: String
)
