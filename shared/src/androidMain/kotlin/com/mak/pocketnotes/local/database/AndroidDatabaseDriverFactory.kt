package com.mak.pocketnotes.local.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.local.database.DatabaseDriverFactory.Companion.LOCAL_DB

internal class AndroidDatabaseDriverFactory(
    private val context: Context
): DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(PocketDatabase.Schema, context, LOCAL_DB)
    }
}