package com.mak.pocketnotes.local

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.mak.pocketnotes.PocketDatabase

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
        )
    }
}