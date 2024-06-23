package com.mak.pocketnotes.local

import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.models.SyncRequest

internal typealias LastSyncEntity = Last_syncs

internal class LastSyncDAO(
    database: PocketDatabase,
    private val dispatcher: Dispatcher
): ILastSyncDAO {
    override suspend fun getLastSyncFor(
        requestType: SyncRequest,
        entityId: String
    ): LastSyncEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun insertLastSync(lastSync: LastSyncEntity) {
        TODO("Not yet implemented")
    }
}

internal interface ILastSyncDAO {
    suspend fun getLastSyncFor(requestType: SyncRequest, entityId: String): LastSyncEntity?
    suspend fun insertLastSync(lastSync: LastSyncEntity)
}
