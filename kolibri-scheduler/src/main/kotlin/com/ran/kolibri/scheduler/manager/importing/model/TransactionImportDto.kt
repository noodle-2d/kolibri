package com.ran.kolibri.scheduler.manager.importing.model

import com.ran.kolibri.common.entity.enums.ExternalTransactionCategory
import com.ran.kolibri.common.entity.enums.TransactionType
import java.math.BigDecimal
import org.joda.time.DateTime

data class TransactionImportDto(
    val accountString: String,
    val type: TransactionType,
    val externalTransactionCategory: ExternalTransactionCategory?,
    val amount: BigDecimal,
    val resultAmount: BigDecimal,
    val date: DateTime,
    val comment: String?,
    val exactFinancialAssetPrice: BigDecimal?,
    val exactSoldCurrencyRatioPart: BigDecimal?,
    val exactBoughtCurrencyRatioPart: BigDecimal?
)
