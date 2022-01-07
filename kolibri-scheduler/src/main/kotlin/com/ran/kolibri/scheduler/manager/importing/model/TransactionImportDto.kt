package com.ran.kolibri.scheduler.manager.importing.model

import com.ran.kolibri.common.entity.enums.TransactionType
import org.joda.time.DateTime
import java.math.BigDecimal

data class TransactionImportDto(
    val accountString: String,
    val type: TransactionType,
    val amount: BigDecimal,
    val resultAmount: BigDecimal,
    val date: DateTime,
    val comment: String,
    val exactFinancialAssetPrice: BigDecimal?,
    val exactSoldCurrencyRatioPart: BigDecimal?,
    val exactBoughtCurrencyRatioPart: BigDecimal?
)
