package com.mak.pocketnotes.local.database

import com.mak.pocketnotes.PocketDatabase

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