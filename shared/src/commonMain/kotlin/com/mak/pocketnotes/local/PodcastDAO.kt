package com.mak.pocketnotes.local

import com.mak.pocketnotes.PocketDatabase

internal class PodcastDAO(
    databaseDriverFactory: DatabaseDriverFactory
): IPodcastDAO {
    private val database = PocketDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.pocketDatabaseQueries

    override fun getBestPodcasts(): List<PodcastEntity> {
        return dbQuery.getBestPodcasts().executeAsList()
    }

    override fun insertPodcast(podcast: PodcastEntity) {
        dbQuery.insertPodcast(podcast)
    }

    override fun insertPodcasts(podcasts: List<PodcastEntity>) {
        podcasts.forEach { podcast ->
            insertPodcast(podcast)
        }
    }

    override fun removePodcasts() {
        dbQuery.removeAllPodcasts()
    }
}

internal interface IPodcastDAO {
    fun getBestPodcasts(): List<PodcastEntity>
    fun insertPodcast(podcast: PodcastEntity)
    fun insertPodcasts(podcasts: List<PodcastEntity>)
    fun removePodcasts()
}