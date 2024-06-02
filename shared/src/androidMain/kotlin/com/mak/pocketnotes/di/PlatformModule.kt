package com.mak.pocketnotes.di

import com.mak.pocketnotes.local.AndroidDatabaseDriverFactory
import com.mak.pocketnotes.local.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(androidContext()) }
}