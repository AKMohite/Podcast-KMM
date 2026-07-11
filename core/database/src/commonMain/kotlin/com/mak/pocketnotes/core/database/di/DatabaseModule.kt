package com.mak.pocketnotes.core.database.di

import app.cash.sqldelight.db.SqlDriver
import com.mak.pocketnotes.core.database.DatabaseTransactionRunner
import com.mak.pocketnotes.core.database.PocketNotesDatabase
import com.mak.pocketnotes.core.database.SQLDatabaseTransactionRunner
import com.mak.pocketnotes.core.database.dao.CuratedPodcastDAO
import com.mak.pocketnotes.core.database.dao.EpisodeDAO
import com.mak.pocketnotes.core.database.dao.GenresDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.database.dao.RelatedPodcastDAO
import com.mak.pocketnotes.core.database.dao.SQLDelightCuratedPodcastDAO
import com.mak.pocketnotes.core.database.dao.SQLDelightEpisodeDAO
import com.mak.pocketnotes.core.database.dao.SQLDelightGenresDAO
import com.mak.pocketnotes.core.database.dao.SQLDelightLastSyncDAO
import com.mak.pocketnotes.core.database.dao.SQLDelightPodcastDAO
import com.mak.pocketnotes.core.database.dao.SQLDelightRelatedPodcastDAO
import com.mak.pocketnotes.core.database.dao.SQLDelightTrendingPodcastDAO
import com.mak.pocketnotes.core.database.dao.TrendingPodcastDAO
import com.mak.pocketnotes.core.database.queries.PocketDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

val localModule = module {
    databasePlatformModule()
    single<DatabaseTransactionRunner> { SQLDatabaseTransactionRunner(get()) }
    single<PocketDatabase> { PocketNotesDatabase(get<SqlDriver>()).build() }
    single<LastSyncDAO> { SQLDelightLastSyncDAO(get(), get()) }
    single<GenresDAO> { SQLDelightGenresDAO(get(), get()) }
    single<PodcastDAO> { SQLDelightPodcastDAO(get(), get()) }
    single<EpisodeDAO> { SQLDelightEpisodeDAO(get(), get()) }
    single<RelatedPodcastDAO> { SQLDelightRelatedPodcastDAO(get(), get()) }
    single<CuratedPodcastDAO> { SQLDelightCuratedPodcastDAO(get(), get()) }
    single<TrendingPodcastDAO> { SQLDelightTrendingPodcastDAO(get(), get()) }
}

internal expect fun Module.databasePlatformModule()
