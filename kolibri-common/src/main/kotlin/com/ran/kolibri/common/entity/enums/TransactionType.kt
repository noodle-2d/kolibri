package com.ran.kolibri.common.entity.enums

import com.ran.kolibri.common.util.PgTypeDescriptor
import kotlin.reflect.KClass

enum class TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER,
    CURRENCY_CONVERSION,
    FINANCIAL_ASSET_PURCHASE,
    FINANCIAL_ASSET_SALE;

    companion object : PgTypeDescriptor<TransactionType> {
        override val klass: KClass<TransactionType> = TransactionType::class
        override val values: Array<TransactionType> = values()
        override val sqlTypeName: String = "transaction_type"
    }
}
