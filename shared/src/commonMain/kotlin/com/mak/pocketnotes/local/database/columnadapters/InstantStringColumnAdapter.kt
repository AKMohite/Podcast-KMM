package com.mak.pocketnotes.local.database.columnadapters

import app.cash.sqldelight.ColumnAdapter
import kotlin.time.Instant

internal object InstantStringColumnAdapter : ColumnAdapter<Instant, Long> {
    override fun decode(databaseValue: Long): Instant = Instant.fromEpochMilliseconds(databaseValue)
    override fun encode(value: Instant): Long = value.toEpochMilliseconds()
}