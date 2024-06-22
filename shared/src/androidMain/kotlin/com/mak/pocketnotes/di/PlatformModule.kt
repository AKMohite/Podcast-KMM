package com.mak.pocketnotes.di

import app.cash.sqldelight.db.SqlDriver
import com.mak.pocketnotes.local.AndroidDatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single<SqlDriver> { AndroidDatabaseDriverFactory(androidContext()).createDriver() }
}