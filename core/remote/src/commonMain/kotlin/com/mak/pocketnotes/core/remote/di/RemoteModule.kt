package com.mak.pocketnotes.core.remote.di

import com.mak.pocketnotes.core.remote.KtorPocketNotesAPI
import com.mak.pocketnotes.core.remote.PocketNotesAPI
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private fun ktorModule(enableNetworkingLogs: Boolean = false) = module {
    single<Json> { createJson() }
    single { createHttpClient(get(), enableNetworkLogs = enableNetworkingLogs) }
}

private val apiModule = module {
    factory<PocketNotesAPI> { KtorPocketNotesAPI(get(), get()) }
}

fun remoteModule() = listOf(ktorModule(), apiModule)