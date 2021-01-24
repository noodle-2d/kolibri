package com.ran.kolibri.common.client.open.exchange.rates.model

import com.typesafe.config.Config

data class OpenExchangeRatesConfig(val apiUrl: String, val appId: String) {
    constructor(config: Config) : this(
        config.getString("open-exchange-rates.api-url"),
        config.getString("open-exchange-rates.app-id")
    )
}
