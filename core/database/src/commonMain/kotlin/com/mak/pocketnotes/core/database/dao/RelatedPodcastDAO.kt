package com.mak.pocketnotes.core.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.queries.PocketDatabase
import com.mak.pocketnotes.core.database.queries.Related_podcasts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal typealias RelatedPodcastEntity = Related_podcasts

internal class RelatedPodcastDAO(
    database: PocketDatabase,
    private val dispatcher: DispatcherProvider
) : IRelatedPodcastDAO {

    private val dbQuery = database.related_podcast_entityQueries

    override fun insertPodcast(podcast: RelatedPodcastEntity) {
        dbQuery.insertPodcast(podcast)
    }

    override fun insertPodcasts(podcasts: List<RelatedPodcastEntity>) {
        podcasts.forEach { podcast ->
            insertPodcast(podcast)
        }
    }

    override suspend fun removePodcasts() = withContext(dispatcher.io) {
        dbQuery.deleteAll().await()
    }

    override fun getPodcast(id: String): Flow<List<PodcastEntity>> {
        return dbQuery.getRelatedPodcasts(id)
            .asFlow()
            .mapToList(dispatcher.io)
    }

    override suspend fun removePodcast(podcastId: String) {
        dbQuery.delete(podcastId)
    }
}

internal interface IRelatedPodcastDAO {
    fun insertPodcast(podcast: RelatedPodcastEntity)
    fun insertPodcasts(podcasts: List<RelatedPodcastEntity>)
    suspend fun removePodcasts(): Long
    fun getPodcast(id: String): Flow<List<PodcastEntity>>
    suspend fun removePodcast(podcastId: String)
}
