package com.ran.kolibri.scheduler.manager.importing.model

import org.joda.time.DateTime
import java.math.BigDecimal

data class ImportedData(
    val financialAssetRows: List<FinancialAssetSheetRow>,
    val accountRows: List<AccountSheetRow>,
    val transactionRows: List<TransactionSheetRow>
)

data class FinancialAssetSheetRow(val name: String, val currencyString: String, val ticker: String?)

data class AccountSheetRow(val name: String, val finalAmount: BigDecimal?, val isClosed: Boolean)

data class TransactionSheetRow(
    val accountName: String,
    val amount: BigDecimal,
    val resultAmount: BigDecimal,
    val comment: String,
    val date: DateTime
)
