package com.mak.pocketnotes.core.database.di

import app.cash.sqldelight.db.SqlDriver
import com.mak.pocketnotes.core.database.AndroidDatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module

internal actual fun Module.databasePlatformModule() {
    single<SqlDriver> { AndroidDatabaseDriverFactory(androidContext()).createDriver() }
}