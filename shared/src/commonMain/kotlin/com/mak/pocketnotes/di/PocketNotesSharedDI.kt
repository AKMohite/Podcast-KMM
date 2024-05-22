package com.mak.pocketnotes.di

import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.remote.PocketNotesAPI
import com.mak.pocketnotes.data.util.provideDispatcher
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.mapper.PodcastMapper
import com.mak.pocketnotes.domain.usecase.GetBestPodcasts
import com.mak.pocketnotes.domain.usecase.GetCuratedPodcasts
import com.mak.pocketnotes.domain.usecase.GetPodcast
import com.mak.pocketnotes.domain.usecase.GetPodcastRecommendations
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private fun networkModule(enableNetworkingLogs: Boolean = false) = module {
    single<Json> { createJson() }
    single { createHttpClient(get(), enableNetworkLogs = enableNetworkingLogs) }
}

private val dataModule = module {
    factory { PodcastMapper() }
    factory { PocketMapper(get()) }
    factory<IPocketNotesAPI> { PocketNotesAPI(get(), get()) }
}

private val utilModule = module {
    factory { provideDispatcher() }
}

private val domainModule = module {
//    single<IRep> {  }
    factory { GetBestPodcasts() }
    factory { GetCuratedPodcasts() }
    factory { GetPodcast() }
    factory { GetPodcastRecommendations() }
}

private val sharedModules = listOf(networkModule(), dataModule, utilModule, domainModule)

fun getSharedModules() = sharedModules