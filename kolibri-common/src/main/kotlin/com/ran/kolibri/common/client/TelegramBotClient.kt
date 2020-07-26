package com.ran.kolibri.common.client

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.config.TelegramBotClientConfig
import com.ran.kolibri.common.dto.ok.OkResponse
import com.ran.kolibri.common.dto.telegram.bot.utils.SendMessageToOwnerBotRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.url

class TelegramBotClient(kodein: Kodein) {

    private val httpClient: HttpClient = kodein.instance()
    private val config: TelegramBotClientConfig = kodein.instance()

    suspend fun sendMessageToOwner(request: SendMessageToOwnerBotRequest): OkResponse =
        httpClient.post {
            url(buildUrl("/send-message-to-owner"))
            body = request
        }

    private fun buildUrl(route: String): String =
        "${config.url}/api/1.x$route"
}
