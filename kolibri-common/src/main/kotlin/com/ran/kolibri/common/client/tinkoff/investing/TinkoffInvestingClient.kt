package com.ran.kolibri.common.client.tinkoff.investing

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.tinkoff.investing.model.CandlesResponse
import com.ran.kolibri.common.client.tinkoff.investing.model.MarketInstrumentListResponse
import com.ran.kolibri.common.client.tinkoff.investing.model.OrderbookResponse
import com.ran.kolibri.common.client.tinkoff.investing.model.TinkoffInvestingConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class TinkoffInvestingClient(kodein: Kodein) {

    private val httpClient: HttpClient = kodein.instance()
    private val config: TinkoffInvestingConfig = kodein.instance()
    private val authorizationHeader = "Bearer ${config.token}"

    suspend fun getOrderbook(figi: String, depth: Int): OrderbookResponse =
        httpClient.get {
            val parameters = mapOf("figi" to figi, "depth" to depth)
            buildRequest("/market/orderbook", parameters)
        }

    suspend fun getCandles(figi: String, from: DateTime, to: DateTime, interval: String): CandlesResponse =
        httpClient.get {
            val fromDateString = DATE_FORMATTER.print(from)
            val toDateString = DATE_FORMATTER.print(to)
            val parameters =
                mapOf("figi" to figi, "from" to fromDateString, "to" to toDateString, "interval" to interval)
            buildRequest("/market/candles", parameters)
        }

    suspend fun searchByTicker(ticker: String): MarketInstrumentListResponse =
        httpClient.get {
            val parameters = mapOf("ticker" to ticker)
            buildRequest("/market/search/by-ticker", parameters)
        }

    private fun HttpRequestBuilder.buildRequest(route: String, parameters: Map<String, Any>) {
        header("Authorization", authorizationHeader)
        url("${config.apiUrl}/openapi$route")
        parameters.forEach {
            parameter(it.key, it.value)
        }
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormat.fullDateTime() // todo: check format matches 2021-10-01T00:00:00Z
    }
}
