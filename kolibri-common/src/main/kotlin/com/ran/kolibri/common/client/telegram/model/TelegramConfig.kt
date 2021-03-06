package com.ran.kolibri.common.client.telegram.model

import com.typesafe.config.Config

data class TelegramConfig(val botApiUrl: String, val botToken: String, val botOwnerId: Int) {
    constructor(config: Config) : this(
        config.getString("telegram.bot.api-url"),
        config.getString("telegram.bot.token"),
        config.getInt("telegram.bot.owner-id")
    )
}
