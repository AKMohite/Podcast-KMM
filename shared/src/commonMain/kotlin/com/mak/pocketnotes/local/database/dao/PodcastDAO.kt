package com.mak.pocketnotes.local.database.dao

import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.local.Podcasts
import kotlinx.coroutines.withContext

internal typealias PodcastEntity = Podcasts

internal class PodcastDAO(
    database: PocketDatabase,
    private val dispatcher: Dispatcher
): IPodcastDAO {
    private val dbQuery = database.podcast_entityQueries

    override suspend fun insertPodcast(podcast: Podcasts) = withContext(dispatcher.io) {
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
    suspend fun insertPodcast(podcast: PodcastEntity)
    suspend fun insertPodcasts(podcasts: List<PodcastEntity>)
    suspend fun removePodcasts()
}