package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.remote.dto.CuratedPodcastDTO
import com.mak.pocketnotes.data.remote.dto.SectionPodcastDTO
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.SectionPodcast
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCuratedPodcasts: KoinComponent {
    private val api: IPocketNotesAPI by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(page: Int): List<CuratedPodcast> {
        val dto = api.getCuratedPodcasts(page).curatedLists ?: return emptyList()
        return dto.toCuratedPodcasts()
    }
}

private fun List<SectionPodcastDTO>.toCuratedPodcasts(): List<CuratedPodcast> {
    return this.map {section ->
        val podcasts = getPodcasts(section.podcasts ?: emptyList())
        CuratedPodcast(
            id = section.id!!,
            title = section.title ?: "",
            podcasts = podcasts
        )
    }
}

private fun getPodcasts(curatedPodcastDTOS: List<CuratedPodcastDTO>): List<SectionPodcast> {
    return curatedPodcastDTOS.map { podcast ->
        SectionPodcast(
            id = podcast.id!!,
            title = podcast.title ?: "",
            image = podcast.thumbnail ?: ""
        )
    }
}
