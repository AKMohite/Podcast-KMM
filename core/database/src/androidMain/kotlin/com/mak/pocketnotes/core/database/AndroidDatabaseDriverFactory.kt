package com.mak.pocketnotes.core.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mak.pocketnotes.core.database.DatabaseDriverFactory.Companion.LOCAL_DB
import com.mak.pocketnotes.core.database.queries.PocketDatabase

internal class AndroidDatabaseDriverFactory(
    private val context: Context
) : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(PocketDatabase.Schema, context, LOCAL_DB)
    }
}