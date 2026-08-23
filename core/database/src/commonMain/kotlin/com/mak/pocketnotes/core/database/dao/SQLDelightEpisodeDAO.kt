package com.mak.pocketnotes.core.database.dao

import app.cash.sqldelight.Query
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.queries.Episodes
import com.mak.pocketnotes.core.database.queries.PocketDatabase
import kotlin.time.Instant
import kotlinx.coroutines.flow.Flow

typealias EpisodeEntity = Episodes

internal class SQLDelightEpisodeDAO(
  database: PocketDatabase,
  private val dispatcher: DispatcherProvider
) : EpisodeDAO {
  private val dbQuery = database.podcast_episode_entityQueries

  override fun getTransacter(): Transacter = dbQuery

  override fun insert(entity: EpisodeEntity) {
    dbQuery.insert(entity)
  }

  override fun insertEpisodes(entities: List<EpisodeEntity>) {
    for (entity in entities) {
      insert(entity)
    }
  }

  override fun getEpisodes(podcastId: String): Flow<List<EpisodeEntity>> = dbQuery
    .getEpisodes(podcastId)
    .asFlow()
    .mapToList(dispatcher.io)

  override fun getEpisodes(podcastId: String, nextEpisodeDate: Instant): Flow<List<EpisodeEntity>> =
    dbQuery
      .getPaginatedEpisodes(podcastId, nextEpisodeDate = nextEpisodeDate)
      .asFlow()
      .mapToList(dispatcher.io)

  override fun getEpisodesQuery(podcastId: String): Query<EpisodeEntity> =
    dbQuery.getEpisodes(podcastId)

  override fun getEpisodesPaginated(
    podcastId: String,
    limit: Long,
    offset: Long
  ): Query<EpisodeEntity> = dbQuery.getEpisodesPaginated(podcastId, limit, offset)

  override fun countEpisodes(podcastId: String): Query<Long> = dbQuery.countEpisodes(podcastId)

  override fun getEpisodesKeyset(
    podcastId: String,
    lastTimestamp: Instant,
    limit: Long
  ): Query<EpisodeEntity> = dbQuery.getEpisodesKeyset(podcastId, lastTimestamp, limit)

  override fun getEpisodesInitial(podcastId: String, limit: Long): Query<EpisodeEntity> =
    dbQuery.getEpisodesInitial(podcastId, limit)

  override fun getEpisodePageBoundaries(
    podcastId: String,
    anchor: Instant?,
    limit: Long
  ): Query<Instant> =
    dbQuery.getEpisodePageBoundaries(limit = limit, anchor = anchor, podcastId = podcastId)

  override fun getEpisodesByBoundary(
    podcastId: String,
    beginInclusive: Instant,
    endExclusive: Instant?
  ): Query<EpisodeEntity> = dbQuery.getEpisodesByBoundary(podcastId, beginInclusive, endExclusive)

  override fun removeEpisodes(podcastId: String) {
    dbQuery.deleteWithId(podcastId)
  }

  override fun removeEpisodes(podcastId: String, nextDate: Instant) {
    dbQuery.removeEpisode(podcastId, nextDate)
  }

  override fun removeEpisodes() {
    dbQuery.deleteAll()
  }
}

interface EpisodeDAO {
  fun getTransacter(): Transacter

  fun insert(entity: EpisodeEntity)

  fun insertEpisodes(entities: List<EpisodeEntity>)

  fun getEpisodes(podcastId: String): Flow<List<EpisodeEntity>>

  fun getEpisodesQuery(podcastId: String): Query<EpisodeEntity>

  fun getEpisodesPaginated(podcastId: String, limit: Long, offset: Long): Query<EpisodeEntity>

  fun countEpisodes(podcastId: String): Query<Long>

  fun getEpisodesKeyset(
    podcastId: String,
    lastTimestamp: Instant,
    limit: Long
  ): Query<EpisodeEntity>

  fun getEpisodesInitial(podcastId: String, limit: Long): Query<EpisodeEntity>

  fun getEpisodePageBoundaries(podcastId: String, anchor: Instant?, limit: Long): Query<Instant>

  fun getEpisodesByBoundary(
    podcastId: String,
    beginInclusive: Instant,
    endExclusive: Instant?
  ): Query<EpisodeEntity>

  fun getEpisodes(podcastId: String, nextEpisodeDate: Instant): Flow<List<EpisodeEntity>>

  fun removeEpisodes(podcastId: String)

  fun removeEpisodes()

  fun removeEpisodes(podcastId: String, nextDate: Instant)
}
