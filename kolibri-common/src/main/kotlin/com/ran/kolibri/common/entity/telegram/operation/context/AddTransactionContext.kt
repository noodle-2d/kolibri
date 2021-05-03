package com.ran.kolibri.common.entity.telegram.operation.context

import java.math.BigDecimal

data class AddTransactionContext(
    val income: IncomeTransactionInfo? = null,
    val expense: ExpenseTransactionInfo? = null
)

data class IncomeTransactionInfo(
    val amount: BigDecimal? = null,
    val accountId: Long? = null,
    val comment: String? = null
)

data class ExpenseTransactionInfo(
    val amount: BigDecimal? = null,
    val accountId: Long? = null,
    val comment: String? = null
)
