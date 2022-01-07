package com.ran.kolibri.common.entity

import com.ran.kolibri.common.entity.enums.Currency
import org.joda.time.DateTime
import java.math.BigDecimal

data class CurrencyPrice(
    val id: Long? = null,
    val currency: Currency,
    val baseCurrency: Currency,
    val price: BigDecimal,
    val date: DateTime
)
