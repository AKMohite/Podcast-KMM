package com.mak.pocketnotes.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal typealias TrendingPodcastEntity = Trending_podcasts

internal class TrendingPodcastDAO(
    databaseDriverFactory: DatabaseDriverFactory,
    private val dispatcher: Dispatcher
): ITrendingPodcastDAO {
    private val database = PocketDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.trending_podcastQueries

    override fun getBestPodcasts(): Flow<List<PodcastEntity>> = dbQuery.getTrendingPodcasts()
        .asFlow()
        .mapToList(dispatcher.io)

    override suspend fun upsertPage(page: Int, entities: List<TrendingPodcastEntity>) = withContext(dispatcher.io) {
        dbQuery.transaction {
            entities.forEach { entity ->
                dbQuery.insertPodcast(entity)
            }
        }
    }
}

internal interface ITrendingPodcastDAO {
    fun getBestPodcasts(): Flow<List<PodcastEntity>>
    suspend fun upsertPage(page: Int, entities: List<TrendingPodcastEntity>)
}
