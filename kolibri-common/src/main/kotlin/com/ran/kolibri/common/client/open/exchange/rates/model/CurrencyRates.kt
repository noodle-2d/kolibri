package com.ran.kolibri.common.client.open.exchange.rates.model

import com.ran.kolibri.common.entity.enums.Currency
import java.math.BigDecimal

data class CurrencyRates(
    val timestamp: Long? = null,
    val base: Currency? = null,
    val rates: Map<String, BigDecimal>? = null
)
