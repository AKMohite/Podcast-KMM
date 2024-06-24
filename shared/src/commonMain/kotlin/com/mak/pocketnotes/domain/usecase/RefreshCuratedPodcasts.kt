package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.remote.dto.CuratedPodcastDTO
import com.mak.pocketnotes.data.remote.dto.SectionPodcastDTO
import com.mak.pocketnotes.domain.models.CuratedPodcast
import com.mak.pocketnotes.domain.models.SectionPodcast
import com.mak.pocketnotes.local.database.DatabaseTransactionRunner
import com.mak.pocketnotes.local.database.dao.CuratedPodcastEntity
import com.mak.pocketnotes.local.database.dao.CuratedSectionEntity
import com.mak.pocketnotes.local.database.dao.ICuratedPodcastDAO
import com.mak.pocketnotes.local.database.dao.IPodcastDAO
import com.mak.pocketnotes.local.database.dao.PodcastEntity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RefreshCuratedPodcasts: KoinComponent {
    private val api: IPocketNotesAPI by inject()
    private val transactionRunner: DatabaseTransactionRunner by inject()
    private val curatedPodcastDAO: ICuratedPodcastDAO by inject()
    private val podcastDAO: IPodcastDAO by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(page: Int): List<CuratedPodcast> {
        val dto = api.getCuratedPodcasts(page).curatedLists ?: return emptyList()
        val (sectionEntities, podcastEntities) = dto.toSectionEntities()
        val podcasts = dto.mapNotNull {
            it.podcasts
        }.flatten()
        .map {
            PodcastEntity(
                id = it.id!!,
                title = it.title ?: "",
                thumbnail = it.thumbnail ?: "",
                image = it.image ?: "",
                description = ""
            )
        }
        transactionRunner {
            curatedPodcastDAO.deletePage(page)
            curatedPodcastDAO.insertCuratedPodcasts(sectionEntities, podcastEntities)
            podcastDAO.insertPodcasts(podcasts)
        }
        return dto.toCuratedPodcasts()
    }
}

private fun List<SectionPodcastDTO>.toSectionEntities(): Pair<List<CuratedSectionEntity>, List<CuratedPodcastEntity>> {
    val sectionEntities = this.map { section ->
        CuratedSectionEntity(
            id = section.id!!,
            title = section.title ?: "",
            page = 1
        )
    }
    val podcastEntities = this.mapNotNull { section ->
        section.podcasts?.map { podcast ->
            CuratedPodcastEntity(
                id = podcast.id!!,
                section_id = section.id!!
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
