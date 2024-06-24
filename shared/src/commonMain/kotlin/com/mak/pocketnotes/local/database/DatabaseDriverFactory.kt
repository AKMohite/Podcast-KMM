package com.mak.pocketnotes.local.database

import app.cash.sqldelight.db.SqlDriver

internal interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver

    companion object {
        const val LOCAL_DB = "pocket_notes.db"
    }
}