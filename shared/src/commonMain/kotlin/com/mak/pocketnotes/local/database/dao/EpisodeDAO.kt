package com.mak.pocketnotes.local.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.local.Episodes
import kotlinx.coroutines.flow.Flow

internal typealias EpisodeEntity = Episodes

internal class EpisodeDAO(
    database: PocketDatabase,
    private val dispatcher: Dispatcher
): IEpisodeDAO {

    private val dbQuery = database.podcast_episode_entityQueries

    override fun insert(entity: EpisodeEntity) {
        dbQuery.insert(entity)
    }

    override fun insertEpisodes(entities: List<EpisodeEntity>) {
        for (entity in entities) {
            insert(entity)
        }
    }

    override fun getEpisodes(podcastId: String): Flow<List<EpisodeEntity>> {
        return dbQuery.getEpisodes(podcastId)
            .asFlow()
            .mapToList(dispatcher.io)
    }

    override fun removeEpisodes(podcastId: String) {
        dbQuery.deleteWithId(podcastId)
    }

    override fun removeEpisodes() {
        dbQuery.deleteAll()
    }
}

internal interface IEpisodeDAO {
    fun insert(entity: EpisodeEntity)
    fun insertEpisodes(entities: List<EpisodeEntity>)
    fun getEpisodes(podcastId: String): Flow<List<EpisodeEntity>>
    fun removeEpisodes(podcastId: String)
    fun removeEpisodes()
}