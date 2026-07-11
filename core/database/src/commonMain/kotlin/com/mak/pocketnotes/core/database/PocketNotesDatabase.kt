package com.mak.pocketnotes.core.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.mak.pocketnotes.core.database.columnadapters.InstantStringColumnAdapter
import com.mak.pocketnotes.core.database.columnadapters.SyncRequestColumnAdapter
import com.mak.pocketnotes.core.database.dao.CuratedPodcastDAO
import com.mak.pocketnotes.core.database.dao.EpisodeDAO
import com.mak.pocketnotes.core.database.dao.GenresDAO
import com.mak.pocketnotes.core.database.dao.LastSyncDAO
import com.mak.pocketnotes.core.database.dao.PodcastDAO
import com.mak.pocketnotes.core.database.dao.RelatedPodcastDAO
import com.mak.pocketnotes.core.database.dao.TrendingPodcastDAO
import com.mak.pocketnotes.core.database.queries.Curated_sections
import com.mak.pocketnotes.core.database.queries.Episodes
import com.mak.pocketnotes.core.database.queries.Genres
import com.mak.pocketnotes.core.database.queries.Last_syncs
import com.mak.pocketnotes.core.database.queries.PocketDatabase
import com.mak.pocketnotes.core.database.queries.Trending_podcasts

internal class PocketNotesDatabase(
    private val driver: SqlDriver
) {
    fun build(): PocketDatabase {
        return PocketDatabase(
            driver = driver,
            trending_podcastsAdapter = Trending_podcasts.Adapter(IntColumnAdapter),
            curated_sectionsAdapter = Curated_sections.Adapter(IntColumnAdapter),
            genresAdapter = Genres.Adapter(
                idAdapter = IntColumnAdapter,
                parent_idAdapter = IntColumnAdapter
            ),
            last_syncsAdapter = Last_syncs.Adapter(
                request_typeAdapter = SyncRequestColumnAdapter,
                timestampAdapter = InstantStringColumnAdapter
            ),
            episodesAdapter = Episodes.Adapter(
                published_onAdapter = InstantStringColumnAdapter,
                next_episode_published_onAdapter = InstantStringColumnAdapter
            )
        )
    }
}

interface PocketNotesDB {
    fun getCuratedPodcastDao(): CuratedPodcastDAO
    fun getPodcastDao(): PodcastDAO
    fun getEpisodeDao(): EpisodeDAO
    fun getGenreDao(): GenresDAO
    fun getLastSyncDao(): LastSyncDAO
    fun getRelatedPodcastDao(): RelatedPodcastDAO
    fun getTrendingPodcastDao(): TrendingPodcastDAO
}
