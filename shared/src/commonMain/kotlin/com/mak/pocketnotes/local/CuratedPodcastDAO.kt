package com.mak.pocketnotes.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal typealias CuratedSectionEntity = Curated_sections
internal typealias CuratedPodcastEntity = Curated_podcasts

internal class CuratedPodcastDAO(
    database: PocketDatabase,
    private val dispatcher: Dispatcher
): ICuratedPodcastDAO {
    private val curatedSectionDao = database.curated_section_entityQueries
    private val curatedPodcastDao = database.curated_podcast_entityQueries

    override suspend fun insertCuratedPodcasts(
        sections: List<CuratedSectionEntity>,
        podcasts: List<CuratedPodcastEntity>
    ) = withContext(dispatcher.io) {
        curatedSectionDao.deletePage(sections.first().page)
        sections.forEach { section ->
            curatedSectionDao.insertCuratedSection(section)
        }
        podcasts.forEach { podcast ->
            curatedPodcastDao.insertCuratedPodcast(podcast)
        }
    }

    override fun getCuratedPodcasts(): Flow<List<CuratedSectionWithPodcast>> {
        return curatedSectionDao.curatedSectionWithPodcast().asFlow()
            .mapToList(dispatcher.io)
    }

}

internal interface ICuratedPodcastDAO {
    suspend fun insertCuratedPodcasts(sections: List<CuratedSectionEntity>, podcasts: List<CuratedPodcastEntity>)
    fun getCuratedPodcasts(): Flow<List<CuratedSectionWithPodcast>>
}
