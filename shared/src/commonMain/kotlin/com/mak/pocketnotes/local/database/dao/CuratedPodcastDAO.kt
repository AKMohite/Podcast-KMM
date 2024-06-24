package com.mak.pocketnotes.local.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.local.CuratedSectionWithPodcast
import com.mak.pocketnotes.local.Curated_podcasts
import com.mak.pocketnotes.local.Curated_sections
import kotlinx.coroutines.flow.Flow

internal typealias CuratedSectionEntity = Curated_sections
internal typealias CuratedPodcastEntity = Curated_podcasts

internal class CuratedPodcastDAO(
    database: PocketDatabase,
    private val dispatcher: Dispatcher
): ICuratedPodcastDAO {
    private val curatedSectionDao = database.curated_section_entityQueries
    private val curatedPodcastDao = database.curated_podcast_entityQueries

    override fun insertCuratedPodcasts(
        sections: List<CuratedSectionEntity>,
        podcasts: List<CuratedPodcastEntity>
    ) {
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

    override fun deletePage(page: Int) {
        curatedSectionDao.deletePage(page)
    }

}

internal interface ICuratedPodcastDAO {
    fun insertCuratedPodcasts(sections: List<CuratedSectionEntity>, podcasts: List<CuratedPodcastEntity>)
    fun getCuratedPodcasts(): Flow<List<CuratedSectionWithPodcast>>
    fun deletePage(page: Int)
}
