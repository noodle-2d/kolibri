package com.ran.kolibri.common.manager

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.telegram.TelegramClient
import com.ran.kolibri.common.client.telegram.model.SendMessageRequest
import com.ran.kolibri.common.client.telegram.model.TelegramConfig
import com.ran.kolibri.common.dao.TelegramIntegrationDao
import com.ran.kolibri.common.util.log

class TelegramManager(kodein: Kodein) {

    private val telegramClient: TelegramClient = kodein.instance()
    private val telegramConfig: TelegramConfig = kodein.instance()
    private val telegramIntegrationDao: TelegramIntegrationDao = kodein.instance()

    suspend fun sendMessageToOwner(text: String) {
        log.info("Sending message to owner: $text")
        val sendMessageRequest = SendMessageRequest(telegramConfig.botOwnerId, text)
        val telegramResponse = telegramClient.sendMessage(sendMessageRequest)
        log.info("Message was successfully sent. Response: $telegramResponse")
    }

    suspend fun pullUpdates() {
        val telegramIntegration = telegramIntegrationDao.get()
        log.info("Current telegram integration: $telegramIntegration")
    }
}
