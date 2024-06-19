package com.mak.pocketnotes.android.di

import com.mak.pocketnotes.android.common.viewmodel.MediaViewModel
import com.mak.pocketnotes.android.feature.home.HomeViewModel
import com.mak.pocketnotes.android.feature.podcastdetail.PodcastDetailViewModel
import com.mak.pocketnotes.android.feature.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { params ->
        PodcastDetailViewModel(getPodcast = get(), podcastRecommendations = get(), podcastId = params.get())
    }
    viewModel{ MediaViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
}