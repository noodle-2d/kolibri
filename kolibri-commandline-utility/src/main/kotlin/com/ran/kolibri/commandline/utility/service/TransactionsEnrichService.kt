package com.ran.kolibri.commandline.utility.service

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dao.TransactionDao
import com.ran.kolibri.common.entity.Transaction
import com.ran.kolibri.common.util.log

class TransactionsEnrichService(kodein: Kodein) {

    private val transactionDao: TransactionDao = kodein.instance()

    suspend fun enrichTransactions(transactions: List<Transaction>) {
        log.info("Enriching transactions")
        // todo
    }
}
