package com.ran.kolibri.telegram.bot.dto.config

import com.typesafe.config.Config

data class TelegramConfig(
    val botApiUrl: String,
    val botToken: String,
    val botUrl: String,
    val botOwnerId: Int
) {
    constructor(config: Config) : this(
        config.getString("telegram.bot.api-url"),
        config.getString("telegram.bot.token"),
        config.getString("telegram.bot.url"),
        config.getInt("telegram.bot.owner-id")
    )
}
