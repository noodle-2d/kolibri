package com.ran.kolibri.scheduler.manager.statistics

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dao.AccountDao
import com.ran.kolibri.common.dao.model.AggregatedAccount
import com.ran.kolibri.common.entity.enums.AccountType
import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.common.manager.TelegramManager
import java.math.BigDecimal

class AccountsStatisticsManager(kodein: Kodein) {

    private val accountDao: AccountDao = kodein.instance()
    private val telegramManager: TelegramManager = kodein.instance()

    suspend fun buildAccountsStatistics() {
        val accounts = accountDao.aggregateActiveAccounts()

        val statisticsList = listOfNotNull(
            buildGroupStatistics(accounts, setOf(AccountType.DEBIT_CARD, AccountType.CREDIT_CARD), "Карты"),
            buildGroupStatistics(accounts, setOf(AccountType.DEPOSIT), "Вклады"),
            buildGroupStatistics(accounts, setOf(AccountType.BROKER), "Брокерские счета"),
            buildGroupStatistics(accounts, setOf(AccountType.CASH), "Наличные"),
            buildGroupStatistics(accounts, setOf(AccountType.CRYPTO), "Криптовалюты"),
            buildGroupStatistics(accounts, setOf(AccountType.DEPT), "Долги"),
            buildGroupStatistics(accounts, setOf(AccountType.FINANCIAL_ASSET), "Ценные бумаги")
        )

        val statisticsString = statisticsList.joinToString("\n\n")
        telegramManager.sendMessageToOwner(statisticsString)
    }

    private fun buildGroupStatistics(
        accounts: List<AggregatedAccount>,
        accountTypes: Set<AccountType>,
        groupName: String
    ): String? {
        val filteredAccounts = accounts
            .filter { accountTypes.contains(it.accountType) }
            .filter { it.currentAmount.compareTo(BigDecimal.ZERO) != 0 }
        if (filteredAccounts.isEmpty()) return null

        val accountStrings = filteredAccounts.map { buildAccountString(it) }
        return "$groupName:\n${accountStrings.joinToString("\n")}"
    }

    private fun buildAccountString(account: AggregatedAccount): String =
        "- ${account.accountName}: " +
            "${account.currentAmount.intValueExact()} " +
            currencyToString(account.accountCurrency)

    private fun currencyToString(currency: Currency?): String =
        when (currency) {
            Currency.USD -> "$"
            Currency.EUR -> "€"
            Currency.RUB -> "₽"
            Currency.CNY -> "Ұ"
            Currency.GBR -> "£"
            Currency.CZK -> "Kč"
            Currency.BTC -> "₿"
            null -> "шт."
        }
}
