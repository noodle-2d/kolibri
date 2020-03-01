package com.ran.kolibri.common.telegram

import com.ran.kolibri.common.telegram.client.TelegramClientConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TelegramConfig {

    @Bean
    fun telegramClientConfig(
            @Value("\${telegram.bot.api.url}") telegramBotApiUrl: String,
            @Value("\${telegram.bot.token}") telegramBotToken: String
    ): TelegramClientConfig {
        return TelegramClientConfig(telegramBotApiUrl, telegramBotToken)
    }
}
