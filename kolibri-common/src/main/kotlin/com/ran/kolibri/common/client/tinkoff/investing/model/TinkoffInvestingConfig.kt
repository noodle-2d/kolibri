package com.ran.kolibri.common.client.tinkoff.investing.model

import com.typesafe.config.Config

data class TinkoffInvestingConfig(val apiUrl: String, val token: String) {
    constructor(config: Config) : this(
        config.getString("tinkoff-investing.api-url"),
        config.getString("tinkoff-investing.token")
    )
}
