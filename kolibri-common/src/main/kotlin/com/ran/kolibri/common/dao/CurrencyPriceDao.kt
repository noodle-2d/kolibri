package com.ran.kolibri.common.dao

import com.ran.kolibri.common.entity.CurrencyPrice

interface CurrencyPriceDao {
    suspend fun insert(currencyPrices: List<CurrencyPrice>): List<CurrencyPrice>
    suspend fun selectAll(): List<CurrencyPrice>
}
