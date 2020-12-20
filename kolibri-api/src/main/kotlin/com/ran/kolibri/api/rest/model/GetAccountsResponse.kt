package com.ran.kolibri.api.rest.model

import com.ran.kolibri.common.entity.enums.AccountType
import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.common.entity.enums.FinancialAssetType
import org.joda.time.DateTime

data class GetAccountsResponse(
    val accounts: List<AccountEntry>,
    val financialAssets: List<FinancialAssetEntry>
)

data class AccountEntry(
    val accountId: Long,
    val accountName: String,
    val currentAmount: Int,
    val accountType: AccountType,
    val accountCurrency: Currency,
    val accountCreateDate: DateTime
)

data class FinancialAssetEntry(
    val accountId: Long,
    val accountName: String,
    val currentAmount: Int,
    val accountCreateDate: DateTime,
    val financialAssetType: FinancialAssetType,
    val financialAssetCurrency: Currency
)
