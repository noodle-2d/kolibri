package com.ran.kolibri.common.dto.config

import com.typesafe.config.Config

data class TelegramClientConfig(
        val apiUrl: String,
        val apiToken: String
) {
    constructor(config: Config) : this(
            config.getString("telegram.api.url"),
            config.getString("telegram.api.token")
    )
}
