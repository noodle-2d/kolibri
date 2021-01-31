package com.ran.kolibri.common.dao

import com.ran.kolibri.common.entity.CurrencyPrice
import org.joda.time.DateTime

interface CurrencyPriceDao {
    suspend fun insert(currencyPrices: List<CurrencyPrice>): List<CurrencyPrice>
    suspend fun selectAll(): List<CurrencyPrice>
    suspend fun selectForDate(date: DateTime): List<CurrencyPrice>
}
