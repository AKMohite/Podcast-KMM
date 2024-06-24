package com.mak.pocketnotes.di

import app.cash.sqldelight.db.SqlDriver
import com.mak.pocketnotes.local.database.IOSDatabaseDriverFactory
import org.koin.dsl.module

actual fun platformModule() = module {
    single<SqlDriver> { IOSDatabaseDriverFactory().createDriver() }
}