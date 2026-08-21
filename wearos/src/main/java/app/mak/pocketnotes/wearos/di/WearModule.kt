package app.mak.pocketnotes.wearos.di

import app.mak.pocketnotes.wearos.feature.details.PodcastDetailViewModel
import app.mak.pocketnotes.wearos.feature.trendingpodcasts.TrendingPodcastsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel {
        TrendingPodcastsViewModel(
            podcastRepository = get()
        )
    }
    viewModel { params ->
        PodcastDetailViewModel(
            podcastRepository = get(),
            podcastId = params.get()
        )
    }
}