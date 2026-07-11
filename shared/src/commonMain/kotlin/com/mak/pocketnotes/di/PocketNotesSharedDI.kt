package com.mak.pocketnotes.di

import com.mak.pocketnotes.core.common.di.commonModule
import com.mak.pocketnotes.core.database.di.localModule
import com.mak.pocketnotes.core.feature.data.home.PodcastMapper
import com.mak.pocketnotes.core.remote.di.ktorModule
import com.mak.pocketnotes.domain.mapper.PocketMapper
import com.mak.pocketnotes.domain.store.BestPodcastsStore
import com.mak.pocketnotes.domain.store.CuratedPodcastsStore
import com.mak.pocketnotes.domain.store.EpisodeStore
import com.mak.pocketnotes.domain.store.PodcastStore
import com.mak.pocketnotes.domain.store.RelatedPodcastsStore
import com.mak.pocketnotes.domain.usecase.GetGenres
import com.mak.pocketnotes.domain.usecase.GetPodcast
import com.mak.pocketnotes.domain.usecase.GetPodcastEpisodes
import com.mak.pocketnotes.domain.usecase.GetPodcastRecommendations
import com.mak.pocketnotes.domain.usecase.RefreshBestPodcasts
import com.mak.pocketnotes.domain.usecase.RefreshCuratedPodcasts
import com.mak.pocketnotes.domain.usecase.SearchPodcast
import org.koin.core.module.Module
import org.koin.dsl.module


private val dataModule = module {
    factory { PodcastMapper() }
    factory { PocketMapper(get()) }
}

private val domainModule = module {
    factory { GetGenres() }
    factory { RefreshBestPodcasts() }
    factory { RefreshCuratedPodcasts() }
    factory { GetPodcast() }
    factory { GetPodcastEpisodes() }
    factory { GetPodcastRecommendations() }
    factory { SearchPodcast() }
}

private val storeModule = module {
    factory { CuratedPodcastsStore() }
    factory { BestPodcastsStore() }
    factory { PodcastStore() }
    factory { EpisodeStore() }
    factory { RelatedPodcastsStore() }
}

internal expect fun platformModule(): Module

private val sharedModules =
    listOf(
        commonModule,
        ktorModule(),
        localModule,
        dataModule,
        storeModule,
        domainModule,
        platformModule()
    )

fun getSharedModules() = sharedModules