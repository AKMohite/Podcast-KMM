package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.remote.dto.CuratedPodcastDTO
import com.mak.pocketnotes.data.remote.dto.SectionPodcastDTO
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.SectionPodcast
import com.mak.pocketnotes.local.CuratedPodcastEntity
import com.mak.pocketnotes.local.CuratedSectionEntity
import com.mak.pocketnotes.local.ICuratedPodcastDAO
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RefreshCuratedPodcasts: KoinComponent {
    private val api: IPocketNotesAPI by inject()
    private val dao: ICuratedPodcastDAO by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(page: Int): List<CuratedPodcast> {
        val dto = api.getCuratedPodcasts(page).curatedLists ?: return emptyList()
        val (sectionEntities, podcastEntities) = dto.toSectionEntities()
        dao.insertCuratedPodcasts(sectionEntities, podcastEntities)
        return dto.toCuratedPodcasts()
    }
}

private fun List<SectionPodcastDTO>.toSectionEntities(): Pair<List<CuratedSectionEntity>, List<CuratedPodcastEntity>> {
    val sectionEntities = this.map { section ->
        CuratedSectionEntity(
            id = section.id!!,
            title = section.title ?: ""
        )
    }
    val podcastEntities = this.mapNotNull { section ->
        section.podcasts?.map { podcast ->
            CuratedPodcastEntity(
                id = podcast.id!!,
                title = podcast.title ?: "",
                thumbnail = podcast.thumbnail ?: "",
                image = podcast.image ?: "",
                sectionId = section.id!!
            )
        }
    }.flatten()
    return sectionEntities to podcastEntities
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
