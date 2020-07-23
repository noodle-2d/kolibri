package com.ran.kolibri.common.dao.impl

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dao.TransactionDao
import com.ran.kolibri.common.entity.Transaction
import com.ran.kolibri.common.entity.enums.ExternalTransactionCategory
import com.ran.kolibri.common.entity.enums.TransactionType
import com.ran.kolibri.common.util.decimal
import com.ran.kolibri.common.util.pgEnum
import com.ran.kolibri.common.util.runTransaction
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.update

class TransactionDaoImpl(kodein: Kodein) : TransactionDao {

    private val db: Database = kodein.instance()

    override suspend fun insertTransactions(transactions: List<Transaction>): List<Transaction> =
        runTransaction(db) {
            transactions.map { transaction ->
                val transactionId = Transactions.insert {
                    it[accountId] = transaction.accountId
                    it[type] = transaction.type
                    it[externalTransactionCategory] = transaction.externalTransactionCategory
                    it[amount] = transaction.amount
                    it[date] = transaction.date
                    it[comment] = transaction.comment
                    it[associatedTransactionId] = transaction.associatedTransactionId
                    it[exactFinancialAssetPrice] = transaction.exactFinancialAssetPrice
                    it[exactSoldCurrencyRatioPart] = transaction.exactSoldCurrencyRatioPart
                    it[exactBoughtCurrencyRatioPart] = transaction.exactBoughtCurrencyRatioPart
                } get Transactions.id
                transaction.copy(id = transactionId.value)
            }
        }

    override suspend fun updateTransaction(transaction: Transaction): Int =
        runTransaction(db) {
            Transactions.update({ Transactions.id eq transaction.id }) {
                it[associatedTransactionId] = transaction.associatedTransactionId
                it[exactFinancialAssetPrice] = transaction.exactFinancialAssetPrice
                it[exactSoldCurrencyRatioPart] = transaction.exactSoldCurrencyRatioPart
                it[exactBoughtCurrencyRatioPart] = transaction.exactBoughtCurrencyRatioPart
            }
        }

    override suspend fun deleteAllTransactions(): Int =
        runTransaction(db) {
            Transactions.deleteAll()
        }
}

object Transactions : LongIdTable("transaction") {
    val accountId = long("account_id")
    val type = pgEnum("type", TransactionType)
    val externalTransactionCategory =
        pgEnum("external_transaction_category", ExternalTransactionCategory).nullable()
    val amount = decimal("amount")
    val date = date("date")
    val comment = varchar("comment", 200).nullable()
    val associatedTransactionId = long("associated_transaction_id").nullable()
    val exactFinancialAssetPrice = decimal("exact_financial_asset_price").nullable()
    val exactSoldCurrencyRatioPart = decimal("exact_sold_currency_ratio_part").nullable()
    val exactBoughtCurrencyRatioPart = decimal("exact_bought_currency_ratio_part").nullable()
}
