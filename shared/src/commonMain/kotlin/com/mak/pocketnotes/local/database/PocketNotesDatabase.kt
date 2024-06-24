package com.mak.pocketnotes.local.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.mak.pocketnotes.PocketDatabase
import com.mak.pocketnotes.local.Curated_sections
import com.mak.pocketnotes.local.Genres
import com.mak.pocketnotes.local.Last_syncs
import com.mak.pocketnotes.local.Trending_podcasts
import com.mak.pocketnotes.local.database.columnadapters.InstantStringColumnAdapter
import com.mak.pocketnotes.local.database.columnadapters.SyncRequestColumnAdapter

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
            )
        )
    }
}
