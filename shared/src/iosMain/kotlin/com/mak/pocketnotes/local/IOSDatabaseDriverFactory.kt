package com.mak.pocketnotes.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.local.DatabaseDriverFactory.Companion.LOCAL_DB

internal class IOSDatabaseDriverFactory: DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(PocketDatabase.Schema, LOCAL_DB)
    }
}