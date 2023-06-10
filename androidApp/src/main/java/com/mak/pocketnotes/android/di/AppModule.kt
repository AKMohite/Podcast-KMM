package com.mak.pocketnotes.android.di

import com.mak.pocketnotes.android.feature.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {
    viewModel { HomeViewModel(get()) }
}