package com.ran.kolibri.common.dto.config

import com.typesafe.config.Config

data class TelegramBotClientConfig(val url: String) {
    constructor(config: Config) : this(config.getString("telegram.bot.client.url"))
}
