package com.mak.pocketnotes.di

import app.cash.sqldelight.db.SqlDriver
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.data.remote.IPocketNotesAPI
import com.mak.pocketnotes.data.remote.PocketNotesAPI
import com.mak.pocketnotes.data.util.provideDispatcher
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.mapper.PodcastMapper
import com.mak.pocketnotes.domain.usecase.GetBestPodcasts
import com.mak.pocketnotes.domain.usecase.GetCuratedPodcasts
import com.mak.pocketnotes.domain.usecase.GetGenres
import com.mak.pocketnotes.domain.usecase.GetPodcast
import com.mak.pocketnotes.domain.usecase.GetPodcastRecommendations
import com.mak.pocketnotes.domain.usecase.RefreshBestPodcasts
import com.mak.pocketnotes.domain.usecase.RefreshCuratedPodcasts
import com.mak.pocketnotes.domain.usecase.SearchPodcast
import com.mak.pocketnotes.local.CuratedPodcastDAO
import com.mak.pocketnotes.local.ICuratedPodcastDAO
import com.mak.pocketnotes.local.IPodcastDAO
import com.mak.pocketnotes.local.ITrendingPodcastDAO
import com.mak.pocketnotes.local.PocketNotesDatabase
import com.mak.pocketnotes.local.PodcastDAO
import com.mak.pocketnotes.local.TrendingPodcastDAO
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

private fun networkModule(enableNetworkingLogs: Boolean = false) = module {
    single<Json> { createJson() }
    single { createHttpClient(get(), enableNetworkLogs = enableNetworkingLogs) }
}

private val localModule = module {
    single<PocketDatabase> { PocketNotesDatabase(get<SqlDriver>()).build() }
    single<IPodcastDAO> { PodcastDAO(get(), get()) }
    single<ICuratedPodcastDAO> { CuratedPodcastDAO(get(), get()) }
    single<ITrendingPodcastDAO> { TrendingPodcastDAO(get(), get()) }
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
    factory { GetGenres() }
    factory { RefreshBestPodcasts() }
    factory { GetBestPodcasts() }
    factory { RefreshCuratedPodcasts() }
    factory { GetCuratedPodcasts() }
    factory { GetPodcast() }
    factory { GetPodcastRecommendations() }
    factory { SearchPodcast() }
}

internal expect fun platformModule(): Module

private val sharedModules = listOf(networkModule(), dataModule, utilModule, domainModule, platformModule(), localModule)

fun getSharedModules() = sharedModules