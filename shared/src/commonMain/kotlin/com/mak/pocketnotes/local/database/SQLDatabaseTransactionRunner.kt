package com.mak.pocketnotes.local.database

import com.mak.pocketnotes.PocketDatabase

/**
 * [Reference](https://github.com/chrisbanes/tivi/blob/ca540a88fc907ed8059c43a81bfd2c0d11b19246/data/db-sqldelight/src/commonMain/kotlin/app/tivi/data/SqlDelightTransactionRunner.kt)
 *
 * SQL transaction runner to have block with queries and that can be roll-backed
 */
internal class SQLDatabaseTransactionRunner(
    private val db: PocketDatabase
): DatabaseTransactionRunner {
    override fun <T> invoke(block: () -> T): T {
        return db.transactionWithResult {
            block()
        }
    }
}

internal interface DatabaseTransactionRunner {
    operator fun <T> invoke(block: () -> T): T
}