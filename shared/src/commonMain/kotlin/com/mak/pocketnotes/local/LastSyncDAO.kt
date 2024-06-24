package com.mak.pocketnotes.local

import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.models.SyncRequest
import com.mak.pocketnotes.local.LastSyncDAO.Companion.DEFAULT_ID
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.time.Duration

internal typealias LastSyncEntity = Last_syncs

internal class LastSyncDAO(
    database: PocketDatabase,
    private val dispatcher: Dispatcher
): ILastSyncDAO {

    private val dbQueries = database.last_sync_entityQueries

    override suspend fun getLastSyncFor(
        requestType: SyncRequest,
        entityId: String
    ): LastSyncEntity? = withContext(dispatcher.io) {
        dbQueries.getLastSyncFor(requestType, entityId).executeAsOneOrNull()
    }

    override suspend fun insertLastSync(requestType: SyncRequest, entityId: String) = withContext(dispatcher.io) {
        dbQueries.insert(
            id = null,
            requestType = requestType,
            entityId = entityId,
            timestamp = Clock.System.now()
        )
    }

    override suspend fun isRequestValid(requestType: SyncRequest, entityId: String, threshold: Duration): Boolean = withContext(dispatcher.io) {
        val lastSync = getLastSyncFor(requestType, entityId) ?: return@withContext false
        val requestBefore = Clock.System.now() - threshold
        return@withContext lastSync.timestamp > requestBefore
    }

    companion object {
        const val DEFAULT_ID = "N/A"
    }

}

internal interface ILastSyncDAO {
    suspend fun getLastSyncFor(requestType: SyncRequest, entityId: String = DEFAULT_ID): LastSyncEntity?
    suspend fun insertLastSync(requestType: SyncRequest, entityId: String = DEFAULT_ID)
    suspend fun isRequestValid(requestType: SyncRequest, entityId: String = DEFAULT_ID, threshold: Duration): Boolean
}
