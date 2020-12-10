package com.ran.kolibri.common.client.telegram

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.telegram.model.SendMessageRequest
import com.ran.kolibri.common.client.telegram.model.TelegramConfig
import com.ran.kolibri.common.client.telegram.model.TelegramResponse
import com.ran.kolibri.common.client.telegram.model.UpdatesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url

class TelegramClient(kodein: Kodein) {

    private val httpClient: HttpClient = kodein.instance()
    private val config: TelegramConfig = kodein.instance()

    suspend fun sendMessage(request: SendMessageRequest): TelegramResponse =
        httpClient.post {
            url(buildUrl("/sendMessage"))
            body = request
        }

    suspend fun getUpdates(offset: Long): UpdatesResponse =
        httpClient.get {
            url("${buildUrl("/getUpdates")}?offset=$offset")
        }

    private fun buildUrl(route: String): String =
        "${config.botApiUrl}/bot${config.botToken}$route"
}
