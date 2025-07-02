package com.mak.pocketnotes.local.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.local.Podcasts
import kotlinx.coroutines.flow.Flow
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
        dbQuery.removeAllPodcasts().await()
    }

    override fun getPodcast(id: String): Flow<PodcastEntity> {
        return dbQuery.getPodcastById(id)
            .asFlow()
            .mapToOne(dispatcher.io)
    }

    override suspend fun removePodcast(podcastId: String): Long {
        return dbQuery.delete(podcastId).await()
    }
}

internal interface IPodcastDAO {
    fun insertPodcast(podcast: PodcastEntity)
    fun insertPodcasts(podcasts: List<PodcastEntity>)
    suspend fun removePodcasts(): Long
    fun getPodcast(id: String): Flow<PodcastEntity>
    suspend fun removePodcast(podcastId: String): Long
}