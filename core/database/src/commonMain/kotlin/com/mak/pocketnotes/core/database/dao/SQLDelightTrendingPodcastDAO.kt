package com.mak.pocketnotes.core.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.queries.PocketDatabase
import com.mak.pocketnotes.core.database.queries.Trending_podcasts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

typealias TrendingPodcastEntity = Trending_podcasts

internal class SQLDelightTrendingPodcastDAO(
    database: PocketDatabase,
    private val dispatcher: DispatcherProvider
) : TrendingPodcastDAO {
    private val dbQuery = database.trending_podcastQueries

    override fun getBestPodcasts(page: Int): Flow<List<PodcastEntity>> =
        dbQuery.getTrendingPodcasts(page)
        .asFlow()
        .mapToList(dispatcher.io)
        .distinctUntilChanged()
        .flowOn(dispatcher.io)

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

interface TrendingPodcastDAO {
    fun getBestPodcasts(page: Int = 1): Flow<List<PodcastEntity>>
    fun upsertPage(entities: List<TrendingPodcastEntity>)
    fun deletePage(page: Int)
    fun deleteAll()
}
