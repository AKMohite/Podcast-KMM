package com.mak.pocketnotes.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class PodcastDAO(
    databaseDriverFactory: DatabaseDriverFactory,
    private val dispatcher: Dispatcher
): IPodcastDAO {
    private val database = PocketDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.podcast_entityQueries

    override fun getBestPodcasts(): Flow<List<PodcastEntity>> = dbQuery.getBestPodcasts()
        .asFlow()
        .mapToList(dispatcher.io)

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
    fun getBestPodcasts(): Flow<List<PodcastEntity>>
    suspend fun insertPodcast(podcast: PodcastEntity)
    suspend fun insertPodcasts(podcasts: List<PodcastEntity>)
    suspend fun removePodcasts()
}