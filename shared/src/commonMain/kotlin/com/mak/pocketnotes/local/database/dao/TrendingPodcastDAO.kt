package com.mak.pocketnotes.local.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.local.Trending_podcasts
import kotlinx.coroutines.flow.Flow

internal typealias TrendingPodcastEntity = Trending_podcasts

internal class TrendingPodcastDAO(
    database: PocketDatabase,
    private val dispatcher: Dispatcher
): ITrendingPodcastDAO {
    private val dbQuery = database.trending_podcastQueries

    override fun getBestPodcasts(): Flow<List<PodcastEntity>> = dbQuery.getTrendingPodcasts()
        .asFlow()
        .mapToList(dispatcher.io)

    override fun upsertPage(entities: List<TrendingPodcastEntity>) {
            entities.forEach { entity ->
                dbQuery.insertPodcast(entity.id, entity.podcast_id, entity.page)
            }
    }

    override fun deletePage(page: Int) {
        dbQuery.deletePage(page)
    }

    override fun deleteAll() {
        dbQuery.deleteAll()
    }
}

internal interface ITrendingPodcastDAO {
    fun getBestPodcasts(): Flow<List<PodcastEntity>>
    fun upsertPage(entities: List<TrendingPodcastEntity>)
    fun deletePage(page: Int)
    fun deleteAll()
}
