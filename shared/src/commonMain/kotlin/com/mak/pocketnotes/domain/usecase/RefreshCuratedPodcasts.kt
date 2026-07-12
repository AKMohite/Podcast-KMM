package com.mak.pocketnotes.domain.usecase

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.CuratedPodcastDAO
import com.mak.pocketnotes.core.database.dao.CuratedPodcastEntity
import com.mak.pocketnotes.core.database.dao.CuratedSectionEntity
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.database.dao.PodcastEntity
import com.mak.pocketnotes.core.feature.domain.home.models.CuratedPodcast
import com.mak.pocketnotes.core.feature.domain.home.models.SectionPodcast
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import com.mak.pocketnotes.core.remote.dto.CuratedPodcastDTO
import com.mak.pocketnotes.core.remote.dto.SectionPodcastDTO
import com.mak.pocketnotes.domain.models.DomainResult
import com.mak.pocketnotes.domain.models.safeCall
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RefreshCuratedPodcasts: KoinComponent {
    private val dispatcher: DispatcherProvider by inject()
    private val api: PocketNotesAPI by inject()
    private val transactionRunner: DatabaseTransactionRunner by inject()
    private val curatedPodcastDAO: CuratedPodcastDAO by inject()
    private val podcastDAO: PodcastDAO by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(page: Int): DomainResult<List<CuratedPodcast>> = safeCall {
        val dto = api.getCuratedPodcasts(page).curatedLists ?: return@safeCall emptyList()
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
                description = "",
                publisher = it.publisher ?: "",
                genres = ""
            )
        }
        updateLocal(page, sectionEntities, podcastEntities, podcasts)
        dto.toCuratedPodcasts()
    }

    private suspend fun updateLocal(
        page: Int,
        sectionEntities: List<CuratedSectionEntity>,
        podcastEntities: List<CuratedPodcastEntity>,
        podcasts: List<PodcastEntity>
    ) = withContext(dispatcher.io) {
        transactionRunner {
            curatedPodcastDAO.deletePage(page)
            curatedPodcastDAO.insertCuratedPodcasts(sectionEntities, podcastEntities)
            podcastDAO.insertPodcasts(podcasts)
        }
    }
}

private fun List<SectionPodcastDTO>.toSectionEntities(): Pair<List<CuratedSectionEntity>, List<CuratedPodcastEntity>> {
    val sectionEntities = this.map { section ->
        CuratedSectionEntity(
            id = section.id!!,
            title = section.title.orEmpty(),
            description = section.description.orEmpty(),
            page = 1
        )
    }
    val podcastEntities = this.mapNotNull { section ->
        section.podcasts?.map { podcast ->
            CuratedPodcastEntity(
                id = "${section.id!!}-${podcast.id!!}",
                podcast_id = podcast.id!!,
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
            title = section.title.orEmpty(),
            description = section.description.orEmpty(),
            podcasts = podcasts
        )
    }
}

private fun getPodcasts(curatedPodcastDTOS: List<CuratedPodcastDTO>): List<SectionPodcast> {
    return curatedPodcastDTOS.map { podcast ->
        SectionPodcast(
            id = podcast.id!!,
            title = podcast.title.orEmpty(),
            thumbnail = podcast.thumbnail.orEmpty(),
            image = podcast.image.orEmpty(),
            publisher = podcast.publisher.orEmpty()
        )
    }
}
