package com.mak.pocketnotes.core.feature.data.di

import com.mak.pocketnotes.core.feature.data.home.repository.OfflineFirstBestPodcastRepository
import com.mak.pocketnotes.core.feature.data.home.repository.OfflineFirstCuratedPodcastRepository
import com.mak.pocketnotes.core.feature.data.podcastdetails.repository.OfflineFirstEpisodeRepository
import com.mak.pocketnotes.core.feature.data.podcastdetails.repository.OfflineFirstPodcastRepository
import com.mak.pocketnotes.core.feature.domain.home.repository.BestPodcastRepository
import com.mak.pocketnotes.core.feature.domain.home.repository.CuratedPodcastRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.EpisodeRepository
import com.mak.pocketnotes.core.feature.domain.podcastdetails.repository.PodcastRepository
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

    factory<CuratedPodcastRepository> {
        OfflineFirstCuratedPodcastRepository(
            api = get(),
            transactionRunner = get(),
            podcastDAO = get(),
            curatedPodcastDAO = get(),
            lastSyncDAO = get(),
            dispatcher = get()
        )
    }

    factory<PodcastRepository> {
        OfflineFirstPodcastRepository(
            api = get(),
            transactionRunner = get(),
            podcastDAO = get(),
            episodeDAO = get(),
            lastSyncDAO = get(),
            dispatcher = get(),
            mapper = get()
        )
    }

    factory<EpisodeRepository> {
        OfflineFirstEpisodeRepository(
            api = get(),
            transactionRunner = get(),
            episodeDAO = get(),
            lastSyncDAO = get(),
            dispatcher = get(),
            mapper = get()
        )
    }
}