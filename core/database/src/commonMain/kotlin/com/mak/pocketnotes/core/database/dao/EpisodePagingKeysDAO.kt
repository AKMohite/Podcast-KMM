package com.mak.pocketnotes.core.database.dao

import com.mak.pocketnotes.core.database.queries.PocketDatabase
import kotlin.time.Instant

interface EpisodePagingKeysDAO {
  fun insertKey(podcastId: String, nextEpisodeDate: Instant?)
  fun getNextEpisodeDate(podcastId: String): Instant?
  fun deleteKey(podcastId: String)
  fun deleteAllKeys()
}

internal class SQLDelightEpisodePagingKeysDAO(
  database: PocketDatabase
) : EpisodePagingKeysDAO {
  private val dbQuery = database.episode_paging_keysQueries

  override fun insertKey(podcastId: String, nextEpisodeDate: Instant?) {
    dbQuery.insertKey(podcastId, nextEpisodeDate)
  }

  override fun getNextEpisodeDate(podcastId: String): Instant? {
    return dbQuery.getNextEpisodeDate(podcastId).executeAsOneOrNull()?.next_episode_date
  }

  override fun deleteKey(podcastId: String) {
    dbQuery.deleteKey(podcastId)
  }

  override fun deleteAllKeys() {
    dbQuery.deleteAllKeys()
  }
}
