package com.ran.kolibri.common.telegram.client

import com.ran.kolibri.common.http.HttpClient
import com.ran.kolibri.common.http.post
import com.ran.kolibri.common.telegram.dto.SendMessageRequest
import com.ran.kolibri.common.telegram.dto.SetWebhookRequest
import com.ran.kolibri.common.telegram.dto.TelegramResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TelegramClient {

    @Autowired
    private lateinit var httpClient: HttpClient
    @Autowired
    private lateinit var config: TelegramClientConfig

    fun setWebhook(request: SetWebhookRequest): TelegramResponse =
            httpClient.post(buildUrl("/setWebhook"), request)

    fun sendMessage(request: SendMessageRequest): TelegramResponse =
            httpClient.post(buildUrl("/sendMessage"), request)

    private fun buildUrl(route: String): String =
            "${config.telegramBotApiUrl}/bot${config.telegramBotToken}${route}"
}
