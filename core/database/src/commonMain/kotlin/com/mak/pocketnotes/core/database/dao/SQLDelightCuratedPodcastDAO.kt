package com.mak.pocketnotes.core.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.queries.CuratedSectionWithPodcast
import com.mak.pocketnotes.core.database.queries.Curated_podcasts
import com.mak.pocketnotes.core.database.queries.Curated_sections
import com.mak.pocketnotes.core.database.queries.PocketDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

typealias CuratedSectionEntity = Curated_sections
typealias CuratedPodcastEntity = Curated_podcasts

internal class SQLDelightCuratedPodcastDAO(
    database: PocketDatabase,
    private val dispatcher: DispatcherProvider
) : CuratedPodcastDAO {
    private val sectionQuery = database.curated_section_entityQueries
    private val podcastQuery = database.curated_podcast_entityQueries

    override fun insertCuratedPodcasts(
        sections: List<CuratedSectionEntity>,
        podcasts: List<CuratedPodcastEntity>
    ) {
        sections.forEach { section ->
            sectionQuery.insertCuratedSection(section)
        }
        podcasts.forEach { podcast ->
            podcastQuery.insertCuratedPodcast(podcast)
        }
    }

    override fun getCuratedPodcasts(): Flow<List<CuratedSectionWithPodcast>> {
        return sectionQuery.curatedSectionWithPodcast().asFlow()
            .distinctUntilChanged()
            .mapToList(dispatcher.io)
            .flowOn(dispatcher.io)
    }

    override fun deletePage(page: Int) {
        sectionQuery.deletePage(page)
    }

}

interface CuratedPodcastDAO {
    fun insertCuratedPodcasts(
        sections: List<CuratedSectionEntity>,
        podcasts: List<CuratedPodcastEntity>
    )

    fun getCuratedPodcasts(): Flow<List<CuratedSectionWithPodcast>>
    fun deletePage(page: Int)
}
