package com.ran.kolibri.api.manager

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.api.rest.model.AccountEntry
import com.ran.kolibri.api.rest.model.FinancialAssetEntry
import com.ran.kolibri.api.rest.model.GetAccountsResponse
import com.ran.kolibri.common.dao.AccountDao
import com.ran.kolibri.common.dao.model.AggregatedAccount
import com.ran.kolibri.common.entity.enums.AccountType
import java.lang.IllegalStateException

class AccountManager(kodein: Kodein) {

    private val accountDao: AccountDao = kodein.instance()

    suspend fun getAccounts(): GetAccountsResponse {
        val aggregatedAccounts = accountDao.aggregateActiveAccounts()
        val usualAccounts = filterUsualAccounts(aggregatedAccounts)
        val financialAssets = filterFinancialAssets(aggregatedAccounts)
        return GetAccountsResponse(usualAccounts, financialAssets)
    }

    private fun filterUsualAccounts(accounts: List<AggregatedAccount>): List<AccountEntry> =
        accounts
            .filter { it.accountType != AccountType.FINANCIAL_ASSET }
            .map {
                AccountEntry(
                    it.accountId,
                    it.accountName,
                    it.currentAmount.intValueExact(),
                    it.accountType,
                    it.accountCurrency
                        ?: throw IllegalStateException("Unknown currency for account ${it.accountId}"),
                    it.accountCreateDate
                )
            }

    private fun filterFinancialAssets(accounts: List<AggregatedAccount>): List<FinancialAssetEntry> =
        accounts
            .filter { it.accountType == AccountType.FINANCIAL_ASSET }
            .map {
                FinancialAssetEntry(
                    it.accountId,
                    it.accountName,
                    it.currentAmount.intValueExact(),
                    it.accountCreateDate,
                    it.financialAssetType
                        ?: throw IllegalStateException("Unknown financial asset type for account ${it.accountId}"),
                    it.financialAssetCurrency
                        ?: throw IllegalStateException("Unknown financial asset currency for account ${it.accountId}")
                )
            }
}
