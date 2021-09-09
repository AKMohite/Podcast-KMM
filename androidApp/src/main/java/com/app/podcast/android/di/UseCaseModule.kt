package com.app.podcast.android.di

import com.app.podcast.data.local.PodcastDB
import com.app.podcast.data.remote.PodcastAPI
import com.app.podcast.domain.interactor.GetBestPodcastUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideBestPodcastUC(
        api: PodcastAPI,
        db: PodcastDB
    ): GetBestPodcastUC = GetBestPodcastUC(api, db)
}