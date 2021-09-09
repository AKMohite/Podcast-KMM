package com.app.podcast.android.di

import android.content.Context
import com.app.podcast.data.local.DatabaseDriverFactory
import com.app.podcast.data.local.PodcastDB
import com.app.podcast.data.remote.PodcastAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun providePodcastAPI(): PodcastAPI = PodcastAPI()

    @Singleton
    @Provides
    fun provideDatabaseDriverFactory(
        @ApplicationContext context: Context
    ): DatabaseDriverFactory = DatabaseDriverFactory(context)

    @Singleton
    @Provides
    fun providePodcastDB(
        databaseDriverFactory: DatabaseDriverFactory
    ): PodcastDB = PodcastDB(databaseDriverFactory)
}