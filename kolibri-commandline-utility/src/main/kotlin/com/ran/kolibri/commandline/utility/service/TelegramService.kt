package com.ran.kolibri.commandline.utility.service

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.config.TelegramConfig
import com.ran.kolibri.common.dto.telegram.SetWebhookRequest
import com.ran.kolibri.common.telegram.TelegramClient
import com.ran.kolibri.common.util.log

class TelegramService(kodein: Kodein) {

    private val telegramClient: TelegramClient = kodein.instance()
    private val telegramConfig: TelegramConfig = kodein.instance()

    suspend fun setWebhook() {
        val webhookUrl = "${telegramConfig.botUrl}/api/1.x/updates/${telegramConfig.botToken}"
        log.info("Setting webhook. Url: $webhookUrl")

        val setWebhookRequest = SetWebhookRequest(webhookUrl, listOf("message"))
        val telegramResponse = telegramClient.setWebhook(setWebhookRequest)
        log.info("Webhook was successfully set. Response: $telegramResponse")
    }
}
