package com.mak.pocketnotes.core.database.dao

import com.mak.pocketnotes.core.common.coroutines.DispatcherProvider
import com.mak.pocketnotes.core.common.models.SyncRequest
import com.mak.pocketnotes.core.database.queries.Last_syncs
import com.mak.pocketnotes.core.database.queries.PocketDatabase
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.Duration

internal typealias LastSyncEntity = Last_syncs

internal class SQLDelightLastSyncDAO(
    database: PocketDatabase,
    private val dispatcher: DispatcherProvider
) : LastSyncDAO {

    private val dbQueries = database.last_sync_entityQueries

    override suspend fun getLastSyncFor(
        requestType: SyncRequest,
        entityId: String
    ): LastSyncEntity? = withContext(dispatcher.io) {
        dbQueries.getLastSyncFor(requestType, entityId).executeAsOneOrNull()
    }

    override fun insertLastSync(requestType: SyncRequest, entityId: String) {
        dbQueries.insert(
            id = null,
            requestType = requestType,
            entityId = entityId,
            timestamp = Clock.System.now()
        )
    }

    override suspend fun isRequestValid(
        requestType: SyncRequest,
        entityId: String,
        threshold: Duration
    ): Boolean = withContext(dispatcher.io) {
        val lastSync = getLastSyncFor(requestType, entityId) ?: return@withContext false
        val requestBefore = Clock.System.now() - threshold
        return@withContext lastSync.timestamp > requestBefore
    }

}

interface LastSyncDAO {
    suspend fun getLastSyncFor(
        requestType: SyncRequest,
        entityId: String = DEFAULT_ID
    ): LastSyncEntity?

    fun insertLastSync(requestType: SyncRequest, entityId: String = DEFAULT_ID)
    suspend fun isRequestValid(
        requestType: SyncRequest,
        entityId: String = DEFAULT_ID,
        threshold: Duration
    ): Boolean

    companion object {
        const val DEFAULT_ID = "N/A"
    }
}
