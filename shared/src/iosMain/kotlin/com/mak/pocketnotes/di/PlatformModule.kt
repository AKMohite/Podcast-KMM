package com.mak.pocketnotes.di

import com.mak.pocketnotes.local.DatabaseDriverFactory
import com.mak.pocketnotes.local.IOSDatabaseDriverFactory
import org.koin.dsl.module

actual fun platformModule() = module {
    single<DatabaseDriverFactory> { IOSDatabaseDriverFactory() }
}