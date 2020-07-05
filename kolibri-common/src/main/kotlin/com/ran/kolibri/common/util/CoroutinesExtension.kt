package com.ran.kolibri.common.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> runIO(ioAction: () -> T): T =
    withContext(Dispatchers.IO) { ioAction() }

suspend fun <T> runTransaction(db: Database, transactionAction: suspend Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, db, statement = transactionAction)
