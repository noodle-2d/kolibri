package com.ran.kolibri.common.entity.enums

import com.ran.kolibri.common.util.PgTypeDescriptor
import kotlin.reflect.KClass

enum class ExternalTransactionCategory {
    ACTIVE_INCOME,
    PASSIVE_INCOME,
    GIFT_INCOME,
    FINANCIAL_EXPENSE,
    NON_FINANCIAL_EXPENSE,
    COMMISSION,
    FINE;

    companion object : PgTypeDescriptor<ExternalTransactionCategory> {
        override val klass: KClass<ExternalTransactionCategory> = ExternalTransactionCategory::class
        override val values: Array<ExternalTransactionCategory> = values()
        override val sqlTypeName: String = "external_transaction_category"
    }
}
