package com.ran.kolibri.common.entity.enums

import com.ran.kolibri.common.util.PgTypeDescriptor
import kotlin.reflect.KClass

enum class AccountType {
    CASH,
    DEPOSIT,
    DEPT,
    DEBIT_CARD,
    CREDIT_CARD,
    FINANCIAL_ASSET,
    OTHER;

    companion object : PgTypeDescriptor<AccountType> {
        override val klass: KClass<AccountType> get() = AccountType::class
        override val values: Array<AccountType> get() = values()
        override val sqlTypeName: String get() = "account_type"
    }
}
