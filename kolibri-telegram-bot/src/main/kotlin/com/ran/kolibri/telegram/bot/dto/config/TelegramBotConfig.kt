package com.ran.kolibri.telegram.bot.dto.config

import com.typesafe.config.Config

data class TelegramBotConfig(
        val botUrl: String,
        val botOwnerId: Int
) {
    constructor(config: Config) : this(
            config.getString("telegram.bot.url"),
            config.getInt("telegram.bot.owner.id")
    )
}
