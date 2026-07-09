package com.mak.pocketnotes.core.remote.di

import com.mak.pocketnotes.core.remote.KtorPocketNotesAPI
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import kotlinx.serialization.json.Json
import org.koin.dsl.module

fun ktorModule(enableNetworkingLogs: Boolean = false) = module {
    single<Json> { createJson() }
    single { createHttpClient(get(), enableNetworkLogs = enableNetworkingLogs) }
    single<PocketNotesAPI> { KtorPocketNotesAPI(get(), get()) }
}