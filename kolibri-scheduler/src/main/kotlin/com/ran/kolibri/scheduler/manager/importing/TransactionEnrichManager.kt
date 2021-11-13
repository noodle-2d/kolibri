package com.ran.kolibri.scheduler.manager.importing

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dao.TransactionDao
import com.ran.kolibri.common.entity.Transaction
import com.ran.kolibri.common.entity.enums.TransactionType
import com.ran.kolibri.common.util.log
import java.lang.IllegalStateException

class TransactionEnrichManager(kodein: Kodein) {

    private val transactionDao: TransactionDao = kodein.instance()

    suspend fun enrichTransactions(transactions: List<Transaction>) {
        log.info("Enriching transactions")

        transactions
            .zipWithNext()
            .forEach { (previousTransaction, nextTransaction) ->
                if (isTransactionToEnrichBy(previousTransaction)) {
                    // validateComments(previousTransaction, nextTransaction)
                    val updatedPreviousTransaction = previousTransaction
                        .copy(associatedTransactionId = nextTransaction.id)
                    val updatedNextTransaction = nextTransaction.copy(
                        associatedTransactionId = previousTransaction.id,
                        exactFinancialAssetPrice = previousTransaction.exactFinancialAssetPrice,
                        exactBoughtCurrencyRatioPart = previousTransaction.exactBoughtCurrencyRatioPart,
                        exactSoldCurrencyRatioPart = previousTransaction.exactSoldCurrencyRatioPart
                    )
                    transactionDao.updateTransaction(updatedPreviousTransaction)
                    transactionDao.updateTransaction(updatedNextTransaction)
                }
            }

        log.info("Transactions enriched successfully")
    }

    // todo: use comments validating later
    private fun validateComments(previousTransaction: Transaction, nextTransaction: Transaction) {
        if (previousTransaction.comment != nextTransaction.comment) {
            throw IllegalStateException(
                "Different comments for associated transactions: " +
                    "$previousTransaction, $nextTransaction"
            )
        }
    }

    // todo: fix this enrichment! sometimes joins wrong transactions
    private fun isTransactionToEnrichBy(transaction: Transaction): Boolean =
        TRANSACTION_TYPES_TO_ENRICH_BY.contains(transaction.type) &&
            transaction.associatedTransactionId == null &&
            (transaction.exactFinancialAssetPrice != null ||
                transaction.exactBoughtCurrencyRatioPart != null &&
                transaction.exactSoldCurrencyRatioPart != null)

    companion object {
        private val TRANSACTION_TYPES_TO_ENRICH_BY = setOf(
            TransactionType.TRANSFER,
            TransactionType.CURRENCY_CONVERSION,
            TransactionType.FINANCIAL_ASSET_SALE,
            TransactionType.FINANCIAL_ASSET_PURCHASE
        )
    }
}
