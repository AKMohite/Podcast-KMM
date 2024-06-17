package com.mak.pocketnotes.local

import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
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

}

internal interface ICuratedPodcastDAO {
    suspend fun insertCuratedPodcasts(sections: List<CuratedSectionEntity>, podcasts: List<CuratedPodcastEntity>)
}
