package com.mak.pocketnotes.core.feature.data.di

import com.mak.pocketnotes.core.feature.data.home.repository.OfflineFirstBestPodcastRepository
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import org.koin.dsl.module

val homeDataModule = module {
    factory<BestPodcastRepository> {
        OfflineFirstBestPodcastRepository(
            api = get(),
            transactionRunner = get(),
            podcastDAO = get(),
            trendingPodcastDAO = get(),
            lastSyncDAO = get(),
            mapper = get(),
            dispatcher = get()
        )
    }
}