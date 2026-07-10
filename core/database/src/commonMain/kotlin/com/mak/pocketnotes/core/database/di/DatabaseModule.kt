package com.mak.pocketnotes.core.database.di

import app.cash.sqldelight.db.SqlDriver
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.PocketNotesDatabase
import com.mak.pocketnotes.core.database.SQLDatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.CuratedPodcastDAO
import com.mak.pocketnotes.core.database.dao.EpisodeDAO
import com.mak.pocketnotes.core.database.dao.GenresDAO
import com.mak.pocketnotes.core.database.dao.ICuratedPodcastDAO
import com.mak.pocketnotes.core.database.dao.IEpisodeDAO
import com.mak.pocketnotes.core.database.dao.IGenresDAO
import com.mak.pocketnotes.core.database.dao.ILastSyncDAO
import com.mak.pocketnotes.core.database.dao.IPodcastDAO
import com.mak.pocketnotes.core.database.dao.IRelatedPodcastDAO
import com.mak.pocketnotes.core.database.dao.ITrendingPodcastDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.database.dao.RelatedPodcastDAO
import com.mak.pocketnotes.core.database.dao.TrendingPodcastDAO
import com.mak.pocketnotes.core.database.queries.PocketDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

val localModule = module {
    databasePlatformModule()
    single<DatabaseTransactionRunner> { SQLDatabaseTransactionRunner(get()) }
    single<PocketDatabase> { PocketNotesDatabase(get<SqlDriver>()).build() }
    single<ILastSyncDAO> { LastSyncDAO(get(), get()) }
    single<IGenresDAO> { GenresDAO(get(), get()) }
    single<IPodcastDAO> { PodcastDAO(get(), get()) }
    single<IEpisodeDAO> { EpisodeDAO(get(), get()) }
    single<IRelatedPodcastDAO> { RelatedPodcastDAO(get(), get()) }
    single<ICuratedPodcastDAO> { CuratedPodcastDAO(get(), get()) }
    single<ITrendingPodcastDAO> { TrendingPodcastDAO(get(), get()) }
}

internal expect fun Module.databasePlatformModule()
