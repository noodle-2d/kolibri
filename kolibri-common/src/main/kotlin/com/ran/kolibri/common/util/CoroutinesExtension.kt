package com.ran.kolibri.common.util

import java.util.concurrent.Future
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> runIO(ioAction: () -> T): T =
    withContext(Dispatchers.IO) { ioAction() }

suspend fun <T> runTransaction(db: Database, transactionAction: suspend Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, db, statement = transactionAction)

suspend fun <T> runFuture(futureAction: () -> Future<T>): T =
    runIO {
        try {
            futureAction().get()
        } catch (e: InterruptedException) {
            throw CancellationException()
        }
    }
