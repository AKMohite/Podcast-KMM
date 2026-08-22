package app.mak.pocketnotes.wearos.di

import app.mak.pocketnotes.wearos.feature.details.PodcastDetailViewModel
import app.mak.pocketnotes.wearos.feature.trendingpodcasts.WearTrendingPodcastsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule =
  module {
    viewModel {
      WearTrendingPodcastsViewModel(
        podcastRepository = get(),
      )
    }
    viewModel { params ->
      PodcastDetailViewModel(
        podcastRepository = get(),
        episodeRepository = get(),
        podcastId = params.get(),
      )
    }
  }
