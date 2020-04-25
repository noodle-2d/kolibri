package com.ran.kolibri.telegram.bot.service

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.config.TelegramClientConfig
import com.ran.kolibri.common.dto.telegram.SendMessageRequest
import com.ran.kolibri.common.dto.telegram.SetWebhookRequest
import com.ran.kolibri.common.telegram.TelegramClient
import com.ran.kolibri.common.util.logInfo
import com.ran.kolibri.telegram.bot.dto.config.TelegramBotConfig
import com.ran.kolibri.telegram.bot.dto.updates.UpdatesRequest

class TelegramService(kodein: Kodein) {

    private val telegramClient: TelegramClient = kodein.instance()
    private val telegramClientConfig: TelegramClientConfig = kodein.instance()
    private val telegramBotConfig: TelegramBotConfig = kodein.instance()

    suspend fun setWebhook() {
        val webhookUrl = "${telegramBotConfig.botUrl}/api/1.x/updates/${telegramClientConfig.apiToken}"
        logInfo("Setting webhook. Url: $webhookUrl")

        val setWebhookRequest = SetWebhookRequest(webhookUrl, listOf("message"))
        val telegramResponse = telegramClient.setWebhook(setWebhookRequest)
        logInfo("Webhook was successfully set. Response: $telegramResponse")
    }

    suspend fun processUpdates(request: UpdatesRequest) {
        logInfo("Processing updates request: $request")

        val sendMessageRequest = SendMessageRequest(request.message!!.chat!!.id, request.message!!.text)
        logInfo("Sending message. Request: $sendMessageRequest")

        val telegramResponse = telegramClient.sendMessage(sendMessageRequest)
        logInfo("Message was successfully sent. Response: $telegramResponse")
    }
}
