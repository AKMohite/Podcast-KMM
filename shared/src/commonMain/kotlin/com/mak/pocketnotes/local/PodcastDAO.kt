package com.mak.pocketnotes.local

import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import kotlinx.coroutines.withContext

internal class PodcastDAO(
    databaseDriverFactory: DatabaseDriverFactory,
    private val dispatcher: Dispatcher
): IPodcastDAO {
    private val database = PocketDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.pocketDatabaseQueries

    override suspend fun getBestPodcasts(): List<PodcastEntity> = withContext(dispatcher.io) {
        dbQuery.getBestPodcasts().executeAsList()
    }

    override suspend fun insertPodcast(podcast: PodcastEntity) = withContext(dispatcher.io) {
        dbQuery.insertPodcast(podcast)
    }

    override suspend fun insertPodcasts(podcasts: List<PodcastEntity>) = withContext(dispatcher.io) {
        podcasts.forEach { podcast ->
            insertPodcast(podcast)
        }
    }

    override suspend fun removePodcasts() = withContext(dispatcher.io) {
        dbQuery.removeAllPodcasts()
    }
}

internal interface IPodcastDAO {
    suspend fun getBestPodcasts(): List<PodcastEntity>
    suspend fun insertPodcast(podcast: PodcastEntity)
    suspend fun insertPodcasts(podcasts: List<PodcastEntity>)
    suspend fun removePodcasts()
}