package com.ran.kolibri.common.dao.impl

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dao.CurrencyPriceDao
import com.ran.kolibri.common.entity.CurrencyPrice
import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.common.util.pgEnum
import com.ran.kolibri.common.util.runTransaction
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.joda.time.DateTime

class CurrencyPriceDaoImpl(kodein: Kodein) : CurrencyPriceDao {

    private val db: Database = kodein.instance()

    override suspend fun insert(currencyPrices: List<CurrencyPrice>): List<CurrencyPrice> =
        runTransaction(db) {
            currencyPrices.map { currencyPrice ->
                val currencyPriceId = CurrencyPrices.insert {
                    it[currency] = currencyPrice.currency
                    it[baseCurrency] = currencyPrice.baseCurrency
                    it[price] = currencyPrice.price
                    it[date] = currencyPrice.date
                } get CurrencyPrices.id
                currencyPrice.copy(id = currencyPriceId.value)
            }
        }

    override suspend fun selectAll(): List<CurrencyPrice> =
        runTransaction(db) {
            CurrencyPrices
                .selectAll()
                .map { mapRow(it) }
        }

    override suspend fun selectForDate(date: DateTime): List<CurrencyPrice> =
        runTransaction(db) {
            CurrencyPrices
                .select { CurrencyPrices.date.eq(date) }
                .map { mapRow(it) }
        }

    private fun mapRow(row: ResultRow): CurrencyPrice =
        CurrencyPrice(
            id = row[CurrencyPrices.id].value,
            currency = row[CurrencyPrices.currency],
            baseCurrency = row[CurrencyPrices.baseCurrency],
            price = row[CurrencyPrices.price],
            date = row[CurrencyPrices.date]
        )
}

object CurrencyPrices : LongIdTable("currency_price") {
    val currency = pgEnum("currency", Currency)
    val baseCurrency = pgEnum("base_currency", Currency)
    val price = decimal("price", 30, 12)
    val date = date("date")
}
