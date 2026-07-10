package com.mak.pocketnotes.core.database.di

import app.cash.sqldelight.db.SqlDriver
import com.mak.pocketnotes.core.database.IOSDatabaseDriverFactory
import org.koin.core.module.Module

internal actual fun Module.databasePlatformModule() {
    single<SqlDriver> { IOSDatabaseDriverFactory().createDriver() }
}