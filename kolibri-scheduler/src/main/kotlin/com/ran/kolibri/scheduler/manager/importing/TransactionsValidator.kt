package com.ran.kolibri.scheduler.manager.importing

import com.ran.kolibri.common.util.log
import com.ran.kolibri.scheduler.manager.importing.model.AccountImportDto
import com.ran.kolibri.scheduler.manager.importing.model.TransactionImportDto
import java.lang.RuntimeException
import java.math.BigDecimal

object TransactionsValidator {

    fun validate(transactions: List<TransactionImportDto>, accounts: List<AccountImportDto>) {
        log.info("Validating transactions")

        val accountAmountsMap = mutableMapOf<String, BigDecimal>()
        accounts.forEach { accountAmountsMap[it.importName] = BigDecimal.ZERO }

        transactions.forEach { transaction ->
            log.info("Validating transaction $transaction")
            val currentAmount = accountAmountsMap[transaction.accountString]!!
            val newAmount = currentAmount.plus(transaction.amount)
            validateAmount(newAmount, transaction)
            accountAmountsMap[transaction.accountString] = newAmount
        }

        log.info("Transactions successfully validated")
    }

    private fun validateAmount(evaluatedAmount: BigDecimal, transaction: TransactionImportDto) {
        if (evaluatedAmount.compareTo(transaction.resultAmount) != 0) {
            throw RuntimeException("Amounts mismatch for transaction $transaction, expected - $evaluatedAmount")
        }
    }
}
