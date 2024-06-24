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

    override fun insertPodcast(podcast: Podcasts) {
        dbQuery.insertPodcast(podcast)
    }

    override fun insertPodcasts(podcasts: List<PodcastEntity>) {
        podcasts.forEach { podcast ->
            insertPodcast(podcast)
        }
    }

    override suspend fun removePodcasts() = withContext(dispatcher.io) {
        dbQuery.removeAllPodcasts()
    }
}

internal interface IPodcastDAO {
    fun insertPodcast(podcast: PodcastEntity)
    fun insertPodcasts(podcasts: List<PodcastEntity>)
    suspend fun removePodcasts()
}