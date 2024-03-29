package com.ran.kolibri.common.entity

import com.ran.kolibri.common.entity.enums.ExternalTransactionCategory
import com.ran.kolibri.common.entity.enums.TransactionType
import org.joda.time.DateTime
import java.math.BigDecimal

data class Transaction(
    val id: Long? = null,
    val accountId: Long,
    val type: TransactionType,
    val externalTransactionCategory: ExternalTransactionCategory?,
    val amount: BigDecimal,
    val date: DateTime,
    val comment: String?,
    val associatedTransactionId: Long?,
    val exactFinancialAssetPrice: BigDecimal?,
    val exactSoldCurrencyRatioPart: BigDecimal?,
    val exactBoughtCurrencyRatioPart: BigDecimal?
)
