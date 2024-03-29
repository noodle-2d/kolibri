package com.ran.kolibri.common.dao.impl

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dao.AccountDao
import com.ran.kolibri.common.dao.model.AggregatedAccount
import com.ran.kolibri.common.entity.Account
import com.ran.kolibri.common.entity.enums.AccountType
import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.common.util.executeSqlStatement
import com.ran.kolibri.common.util.pgEnum
import com.ran.kolibri.common.util.runTransaction
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.leftJoin
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.sum
import java.math.BigDecimal

class AccountDaoImpl(kodein: Kodein) : AccountDao {

    private val db: Database = kodein.instance()

    override suspend fun insertAccounts(accounts: List<Account>): List<Account> =
        runTransaction(db) {
            accounts.map { account ->
                val accountId = Accounts.insert {
                    it[name] = account.name
                    it[type] = account.type
                    it[currency] = account.currency
                    it[financialAssetId] = account.financialAssetId
                    it[createDate] = account.createDate
                    it[closeDate] = account.closeDate
                } get Accounts.id
                account.copy(id = accountId.value)
            }
        }

    override suspend fun selectAll(): List<Account> =
        runTransaction(db) {
            Accounts.selectAll().map { mapRow(it) }
        }

    override suspend fun aggregateActiveAccounts(): List<AggregatedAccount> =
        runTransaction(db) {
            val transactionsAccountId = Transactions.accountId.alias("account_id")
            val transactionsTotalAmount = Transactions.amount.sum().alias("total_amount")
            val groupedTransactions = Transactions
                .slice(transactionsAccountId, transactionsTotalAmount)
                .selectAll()
                .groupBy(Transactions.accountId)
                .alias("tt")
            Accounts
                .leftJoin(FinancialAssets, { this.financialAssetId }, { this.id })
                .leftJoin(groupedTransactions, { Accounts.id }, { groupedTransactions[transactionsAccountId] })
                .slice(
                    Accounts.id,
                    Accounts.name,
                    groupedTransactions[transactionsTotalAmount],
                    Accounts.type,
                    Accounts.currency,
                    Accounts.createDate,
                    FinancialAssets.type,
                    FinancialAssets.currency
                )
                .select { Accounts.closeDate.isNull() }
                .orderBy(Accounts.id)
                .map {
                    AggregatedAccount(
                        it[Accounts.id].value,
                        it[Accounts.name],
                        it[groupedTransactions[transactionsTotalAmount]] ?: BigDecimal.ZERO,
                        it[Accounts.type],
                        it[Accounts.currency],
                        it[Accounts.createDate],
                        it[FinancialAssets.type],
                        it[FinancialAssets.currency]
                    )
                }
        }

    override suspend fun deleteAllAccounts(): Int =
        runTransaction(db) {
            Accounts.deleteAll()
        }

    override suspend fun restartIdSequence() =
        runTransaction(db) {
            executeSqlStatement("alter sequence account_id_seq restart")
        }

    private fun mapRow(row: ResultRow): Account =
        Account(
            id = row[Accounts.id].value,
            name = row[Accounts.name],
            type = row[Accounts.type],
            currency = row[Accounts.currency],
            financialAssetId = row[Accounts.financialAssetId],
            createDate = row[Accounts.createDate],
            closeDate = row[Accounts.closeDate]
        )
}

object Accounts : LongIdTable("account") {
    val name = varchar("name", 200)
    val type = pgEnum("type", AccountType)
    val currency = pgEnum("currency", Currency).nullable()
    val financialAssetId = long("financial_asset_id").nullable()
    val createDate = date("create_date")
    val closeDate = date("close_date").nullable()
}
