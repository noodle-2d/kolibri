package com.ran.kolibri.scheduler.manager.statistics

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dao.AccountDao
import com.ran.kolibri.common.dao.model.AggregatedAccount
import com.ran.kolibri.common.entity.enums.AccountType
import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.common.entity.enums.FinancialAssetType
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.scheduler.manager.telegram.SingleActionUpdateProcessor
import com.ran.kolibri.scheduler.manager.telegram.model.TelegramOperationType
import java.math.BigDecimal

class AccountsStatisticsManager(kodein: Kodein) : SingleActionUpdateProcessor {

    private val accountDao: AccountDao = kodein.instance()
    private val telegramManager: TelegramManager = kodein.instance()

    override val operationType: TelegramOperationType
        get() = TelegramOperationType.SHOW_ACCOUNTS_STAT

    override suspend fun doProcessUpdate() {
        val accounts = accountDao
            .aggregateActiveAccounts()
            .filter { it.currentAmount.compareTo(BigDecimal.ZERO) != 0 }

        val statisticsList = listOfNotNull(
            buildStatistics(filterAccounts(accounts, setOf(AccountType.DEBIT_CARD, AccountType.CREDIT_CARD)), "Карты"),
            buildStatistics(filterAccounts(accounts, setOf(AccountType.DEPOSIT)), "Вклады"),
            buildStatistics(filterAccounts(accounts, setOf(AccountType.BROKER)), "Брокерские счета"),
            buildStatistics(filterAccounts(accounts, setOf(AccountType.CASH)), "Наличные"),
            buildStatistics(filterAccounts(accounts, setOf(AccountType.CRYPTO)), "Криптовалюты"),
            buildStatistics(filterAccounts(accounts, setOf(AccountType.DEPT)), "Долги"),
            buildStatistics(filterFinancialAssets(accounts, FinancialAssetType.STOCK), "Акции"),
            buildStatistics(filterFinancialAssets(accounts, FinancialAssetType.BOND), "Облигации"),
            buildStatistics(filterFinancialAssets(accounts, FinancialAssetType.FUND), "Фонды"),
            buildStatistics(filterFinancialAssets(accounts, FinancialAssetType.OPTION), "Опционы")
        )

        val statisticsString = statisticsList.joinToString("\n\n")
        telegramManager.sendMessageToOwner(statisticsString)
    }

    private fun filterAccounts(
        accounts: List<AggregatedAccount>,
        accountTypes: Set<AccountType>
    ): List<AggregatedAccount> =
        accounts
            .filter { accountTypes.contains(it.accountType) }
            .filter { it.currentAmount.compareTo(BigDecimal.ZERO) != 0 }

    private fun filterFinancialAssets(
        accounts: List<AggregatedAccount>,
        financialAssetType: FinancialAssetType
    ): List<AggregatedAccount> =
        accounts
            .filter { it.financialAssetType == financialAssetType }
            .filter { it.currentAmount.compareTo(BigDecimal.ZERO) != 0 }

    private fun buildStatistics(accounts: List<AggregatedAccount>, groupName: String): String? {
        if (accounts.isEmpty()) return null
        val accountStrings = accounts.map { buildAccountString(it) }
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
            Currency.GBP -> "£"
            Currency.CZK -> "Kč"
            Currency.BTC -> "₿"
            null -> "шт."
        }
}
