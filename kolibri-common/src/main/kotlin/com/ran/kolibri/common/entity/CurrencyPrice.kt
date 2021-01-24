package com.ran.kolibri.common.entity

import com.ran.kolibri.common.entity.enums.Currency
import java.math.BigDecimal
import org.joda.time.DateTime

data class CurrencyPrice(
    val id: Long? = null,
    val currency: Currency,
    val baseCurrency: Currency,
    val price: BigDecimal,
    val date: DateTime
)
