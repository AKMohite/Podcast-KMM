package app.mak.pocketnotes.wearos.di

import app.mak.pocketnotes.wearos.feature.trendingpodcasts.TrendingPodcastsViewModel
import org.koin.dsl.module

val viewmodelModule = module {
    factory {
        TrendingPodcastsViewModel(
            podcastRepository = get()
        )
    }
}