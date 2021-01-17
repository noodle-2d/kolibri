package com.ran.kolibri.common.entity.enums

import com.ran.kolibri.common.util.PgTypeDescriptor
import kotlin.reflect.KClass

enum class Currency {
    USD,
    EUR,
    RUB,
    CNY,
    GBP,
    CZK,
    BTC;

    companion object : PgTypeDescriptor<Currency> {
        override val klass: KClass<Currency> get() = Currency::class
        override val values: Array<Currency> get() = values()
        override val sqlTypeName: String get() = "currency"
    }
}
