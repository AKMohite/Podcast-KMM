package com.mak.pocketnotes.android.di

import com.mak.pocketnotes.android.common.viewmodel.MediaViewModel
import com.mak.pocketnotes.android.feature.discover.DiscoverViewmodel
import com.mak.pocketnotes.android.feature.player.v2.PlayerViewModel
import com.mak.pocketnotes.android.feature.podcastdetail.PodcastDetailViewModel
import com.mak.pocketnotes.android.feature.queue.QueueViewModel
import com.mak.pocketnotes.android.feature.search.SearchViewModel
import com.mak.pocketnotes.android.feature.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {
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
    viewModel{
         MediaViewModel(
             serviceHandler = get(),
             savedStateHandle = get()
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