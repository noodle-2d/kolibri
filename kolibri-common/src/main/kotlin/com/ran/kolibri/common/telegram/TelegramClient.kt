package com.ran.kolibri.common.telegram

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.config.TelegramClientConfig
import com.ran.kolibri.common.dto.telegram.SendMessageRequest
import com.ran.kolibri.common.dto.telegram.SetWebhookRequest
import com.ran.kolibri.common.dto.telegram.TelegramResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.url

class TelegramClient(kodein: Kodein) {

    private val httpClient: HttpClient = kodein.instance()
    private val config: TelegramClientConfig = kodein.instance()

    suspend fun setWebhook(request: SetWebhookRequest): TelegramResponse =
        httpClient.post {
            url(buildUrl("/setWebhook"))
            body = request
        }

    suspend fun sendMessage(request: SendMessageRequest): TelegramResponse =
        httpClient.post {
            url(buildUrl("/sendMessage"))
            body = request
        }

    private fun buildUrl(route: String): String =
        "${config.apiUrl}/bot${config.apiToken}$route"
}
