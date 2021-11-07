package com.ran.kolibri.common.client.open.exchange.rates

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.open.exchange.rates.model.CurrencyRates
import com.ran.kolibri.common.client.open.exchange.rates.model.OpenExchangeRatesConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class OpenExchangeRatesClient(kodein: Kodein) {

    private val httpClient: HttpClient = kodein.instance()
    private val config: OpenExchangeRatesConfig = kodein.instance()

    suspend fun getLatestRates(): CurrencyRates =
        httpClient.get {
            buildRequest("/latest.json")
        }

    suspend fun getHistoricalRates(date: DateTime): CurrencyRates =
        httpClient.get {
            val dateString = DATE_FORMATTER.print(date)
            buildRequest("/historical/$dateString.json")
        }

    private fun HttpRequestBuilder.buildRequest(route: String) {
        url("${config.apiUrl}/api$route")
        parameter("app_id", config.appId)
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd")
    }
}
