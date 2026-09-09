package com.mak.pocketnotes.core.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.database.queries.PocketDatabase
import kotlin.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SQLDelightSubscriptionDAO(
  database: PocketDatabase,
  private val dispatcher: DispatcherProvider
) : SubscriptionDAO {
  private val dbQuery = database.subscription_entityQueries

  override fun subscribe(podcastId: String, subscribedAt: Instant) {
    dbQuery.insertSubscription(podcastId, subscribedAt)
  }

  override fun unsubscribe(podcastId: String) {
    dbQuery.deleteSubscription(podcastId)
  }

  override fun isSubscribed(podcastId: String): Flow<Boolean> = dbQuery
    .isSubscribed(podcastId)
    .asFlow()
    .mapToOne(dispatcher.io)
    .map { it > 0 }

  override fun getSubscribedPodcasts(): Flow<List<PodcastEntity>> = dbQuery
    .getSubscribedPodcasts()
    .asFlow()
    .mapToList(dispatcher.io)
}

interface SubscriptionDAO {
  fun subscribe(podcastId: String, subscribedAt: Instant)
  fun unsubscribe(podcastId: String)
  fun isSubscribed(podcastId: String): Flow<Boolean>
  fun getSubscribedPodcasts(): Flow<List<PodcastEntity>>
}
