package com.mak.pocketnotes.local.database.columnadapters

import app.cash.sqldelight.ColumnAdapter
import com.mak.pocketnotes.domain.models.SyncRequest

internal object SyncRequestColumnAdapter: ColumnAdapter<SyncRequest, String> {
    override fun decode(databaseValue: String): SyncRequest {
        return SyncRequest.entries.first { it.name == databaseValue }
    }

    override fun encode(value: SyncRequest): String = value.name

}