package com.ran.kolibri.scheduler.manager.prices

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.open.exchange.rates.OpenExchangeRatesClient
import com.ran.kolibri.common.client.sheets.SheetsClient
import com.ran.kolibri.common.client.sheets.model.GoogleConfig
import com.ran.kolibri.common.client.sheets.model.SheetRange
import com.ran.kolibri.common.client.sheets.model.SheetRow
import com.ran.kolibri.common.dao.CurrencyPriceDao
import com.ran.kolibri.common.entity.CurrencyPrice
import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.common.util.log
import com.ran.kolibri.scheduler.manager.telegram.TelegramBotNotifyingUtils
import java.lang.IllegalStateException
import java.math.BigDecimal
import java.math.RoundingMode
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class CurrencyPricesManager(kodein: Kodein) : TelegramBotNotifyingUtils {

    private val currencyPriceDao: CurrencyPriceDao = kodein.instance()
    private val openExchangeRatesClient: OpenExchangeRatesClient = kodein.instance()

    override val telegramManager: TelegramManager = kodein.instance()

    private val googleConfig: GoogleConfig = kodein.instance()
    private val sheetsClient: SheetsClient = kodein.instance()

    suspend fun updateCurrencyPricesWithNotification() =
        doActionSendingMessageToOwner("updating currency prices") { updateCurrencyPrices() }

    suspend fun updateCurrencyPrices(): String {
        val storedCurrencyDates = getStoredCurrencyDates()
        val datesList = buildDatesList()

        datesList.forEach { date ->
            if (isNeededToRequestForDate(date, storedCurrencyDates)) {
                log.info("Requesting currency prices for date $date")
                val dateCurrencyPrices = requestCurrencyPricesForDate(date)
                log.info("Storing currency prices for date $date: $dateCurrencyPrices")
                storeCurrencyPrices(dateCurrencyPrices, storedCurrencyDates)
            }
        }

        updateCurrencyPricesInSheets()
        return "Updated currency prices"
    }

    private suspend fun getStoredCurrencyDates(): Map<Long, List<Currency>> =
        currencyPriceDao.selectAll()
            .groupBy { it.date.millis }
            .mapValues { currencyEntry -> currencyEntry.value.map { it.currency } }

    private fun buildDatesList(): List<DateTime> {
        val daysQuantity = (DateTime.now().withTimeAtStartOfDay().millis - WATCH_START_DATE.millis) / MILLIS_IN_DAY
        return (0..daysQuantity.toInt()).map { day -> WATCH_START_DATE.plusDays(day) }
    }

    private fun isNeededToRequestForDate(date: DateTime, storedCurrencyDates: Map<Long, List<Currency>>): Boolean {
        val dateCurrencies = storedCurrencyDates[date.millis] ?: listOf()
        return !dateCurrencies.containsAll(WATCHED_CURRENCIES)
    }

    private suspend fun requestCurrencyPricesForDate(date: DateTime): List<CurrencyPrice> {
        val currencyRates = if (isToday(date)) openExchangeRatesClient.getLatestRates()
        else openExchangeRatesClient.getHistoricalRates(date)
        val ratesMap = currencyRates.rates ?: mapOf()

        return WATCHED_CURRENCIES.map { currency ->
            CurrencyPrice(
                currency = currency,
                baseCurrency = currencyRates.base ?: emptyFieldError("base"),
                price = ratesMap[currency.name] ?: emptyFieldError("rates.${currency.name}"),
                date = date
            )
        }
    }

    private fun isToday(date: DateTime): Boolean =
        date.isBeforeNow && date.plusDays(1).isAfterNow

    private suspend fun storeCurrencyPrices(
        currencyPrices: List<CurrencyPrice>,
        storedCurrencyDates: Map<Long, List<Currency>>
    ) {
        val dateCurrencies = storedCurrencyDates[currencyPrices.first().date.millis] ?: listOf()
        val currencyPricesToStore = currencyPrices.filter { !dateCurrencies.contains(it.currency) }
        currencyPriceDao.insert(currencyPricesToStore)
    }

    private fun emptyFieldError(field: String): Nothing =
        throw IllegalStateException("Field $field is empty")

    private suspend fun updateCurrencyPricesInSheets() {
        log.info("Updating currency prices in sheets")
        val today = DateTime.now().withTimeAtStartOfDay()
        val todayCurrencyPrices = currencyPriceDao.selectForDate(today)

        updateSheetsValue("G18", retrieveUsdPriceString(todayCurrencyPrices))
        updateSheetsValue("G19", retrieveEurPriceString(todayCurrencyPrices))
        updateSheetsValue("G20", retrieveCnyPriceString(todayCurrencyPrices))
        updateSheetsValue("H21", retrieveBtcPriceString(todayCurrencyPrices))
        updateSheetsValue("N17", today.toString(CURRENCY_PRICE_DATE_FORMAT))

        log.info("Currency prices were successfully updated in sheets")
    }

    private suspend fun updateSheetsValue(cell: String, value: String) {
        val sheetRow = SheetRow(listOf(value))
        val sheetRange = SheetRange(listOf(sheetRow))
        val range = "Счета!$cell"

        log.info("Updating value at cell $cell to $value")
        sheetsClient.updateRange(googleConfig.accountsSpreadsheetId, range, sheetRange)
    }

    private fun retrieveUsdPriceString(currencyPrices: List<CurrencyPrice>): String =
        buildCurrencyDivisionString(currencyPrices, Currency.RUB, Currency.USD)

    private fun retrieveEurPriceString(currencyPrices: List<CurrencyPrice>): String =
        buildCurrencyDivisionString(currencyPrices, Currency.RUB, Currency.EUR)

    private fun retrieveCnyPriceString(currencyPrices: List<CurrencyPrice>): String =
        buildCurrencyDivisionString(currencyPrices, Currency.RUB, Currency.CNY)

    private fun retrieveBtcPriceString(currencyPrices: List<CurrencyPrice>): String =
        buildCurrencyDivisionString(currencyPrices, Currency.USD, Currency.BTC)

    private fun buildCurrencyDivisionString(
        currencyPrices: List<CurrencyPrice>,
        currencyNumerator: Currency,
        currencyDenominator: Currency
    ): String =
        retrieveCurrencyPrice(currencyPrices, currencyNumerator)
            .divide(retrieveCurrencyPrice(currencyPrices, currencyDenominator), RoundingMode.HALF_UP)
            .let { toSheetsString(it) }

    private fun retrieveCurrencyPrice(currencyPrices: List<CurrencyPrice>, currency: Currency): BigDecimal =
        when (currency) {
            Currency.USD -> BigDecimal.ONE
            else -> currencyPrices
                .find { it.currency == currency }!!
                .price
        }

    private fun toSheetsString(number: BigDecimal): String =
        number.setScale(2, RoundingMode.HALF_UP)
            .toString()
            .replace(".", ",")

    companion object {
        private val WATCH_START_DATE = DateTime.parse("2018-01-01T00:00:00Z")
        private const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000
        private val WATCHED_CURRENCIES = listOf(
            Currency.RUB,
            Currency.EUR,
            Currency.CNY,
            Currency.GBP,
            Currency.CZK,
            Currency.BTC
        )

        private val CURRENCY_PRICE_DATE_FORMAT = DateTimeFormat.forPattern("dd.MM.yyyy")
    }
}
