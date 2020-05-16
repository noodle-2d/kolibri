package com.ran.kolibri.commandline.utility.service

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.config.TelegramConfig
import com.ran.kolibri.common.dto.telegram.SetWebhookRequest
import com.ran.kolibri.common.telegram.TelegramClient
import com.ran.kolibri.common.util.logInfo

class TelegramService(kodein: Kodein) {

    private val telegramClient: TelegramClient = kodein.instance()
    private val telegramConfig: TelegramConfig = kodein.instance()

    suspend fun setWebhook() {
        val webhookUrl = "${telegramConfig.botUrl}/api/1.x/updates/${telegramConfig.botToken}"
        logInfo("Setting webhook. Url: $webhookUrl")

        val setWebhookRequest = SetWebhookRequest(webhookUrl, listOf("message"))
        val telegramResponse = telegramClient.setWebhook(setWebhookRequest)
        logInfo("Webhook was successfully set. Response: $telegramResponse")
    }
}
