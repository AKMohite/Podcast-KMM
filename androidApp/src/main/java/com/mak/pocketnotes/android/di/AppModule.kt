package com.mak.pocketnotes.android.di

import com.mak.pocketnotes.android.feature.discover.DiscoverViewmodel
import com.mak.pocketnotes.android.feature.player.v2.PlayerViewModel
import com.mak.pocketnotes.android.feature.podcastdetail.PodcastDetailViewModel
import com.mak.pocketnotes.android.feature.queue.QueueViewModel
import com.mak.pocketnotes.android.feature.search.SearchViewModel
import com.mak.pocketnotes.android.feature.settings.SettingsViewModel
import com.mak.pocketnotes.android.media.ExoPlayerController
import com.mak.pocketnotes.media.PlayerController
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {

    single<PlayerController> { ExoPlayerController(get(), get()) }

    viewModel {
        DiscoverViewmodel(
            refreshBestPodcasts = get(),
            refreshCuratedPodcasts = get(),
            getBestPodcasts = get(),
            getCuratedPodcasts = get()
        )
    }
    viewModel {
        QueueViewModel(
            controller = get()
        )
    }
    viewModel {
        PlayerViewModel(
            controller = get()
        )
    }
    viewModel { params ->
        PodcastDetailViewModel(
            getPodcast = get(),
            podcastRecommendations = get(),
            podcastEpisodes = get(),
            podcastId = params.get()
        )
    }
    viewModel {
        SearchViewModel(
            getGenres = get(),
            searchPodcast = get(),
            getBestPodcasts = get()
        )
    }
    viewModel {
        SettingsViewModel(repository = get())
    }
}