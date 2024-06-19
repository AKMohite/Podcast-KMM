package com.mak.pocketnotes.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class CuratedPodcastDAO(
    databaseDriverFactory: DatabaseDriverFactory,
    private val dispatcher: Dispatcher
): ICuratedPodcastDAO {
    private val database = PocketDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.pocketDatabaseQueries

    override suspend fun insertCuratedPodcasts(
        sections: List<CuratedSectionEntity>,
        podcasts: List<CuratedPodcastEntity>
    ) = withContext(dispatcher.io) {
        sections.forEach { section ->
            dbQuery.insertCuratedSection(section)
        }
        podcasts.forEach { podcast ->
            dbQuery.insertCuratedPodcast(podcast)
        }
    }

    override fun getCuratedPodcasts(): Flow<List<CuratedSectionWithPodcast>> {
        return dbQuery.curatedSectionWithPodcast().asFlow()
            .mapToList(dispatcher.io)
    }

}

internal interface ICuratedPodcastDAO {
    suspend fun insertCuratedPodcasts(sections: List<CuratedSectionEntity>, podcasts: List<CuratedPodcastEntity>)
    fun getCuratedPodcasts(): Flow<List<CuratedSectionWithPodcast>>
}
