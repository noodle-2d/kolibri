package com.ran.kolibri.telegram.bot.service

import com.ran.kolibri.common.telegram.client.TelegramClient
import com.ran.kolibri.common.telegram.client.TelegramClientConfig
import com.ran.kolibri.common.telegram.dto.SendMessageRequest
import com.ran.kolibri.common.telegram.dto.SetWebhookRequest
import com.ran.kolibri.common.util.logInfo
import com.ran.kolibri.telegram.bot.dto.updates.UpdatesRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TelegramService {

    @Autowired
    private lateinit var telegramClient: TelegramClient
    @Autowired
    private lateinit var telegramClientConfig: TelegramClientConfig

    @Value("\${telegram.bot.url}")
    private lateinit var botUrl: String
    @Value("\${telegram.bot.owner.id}")
    private lateinit var ownerId: String

    fun setWebhook() {
        val webhookUrl = "$botUrl/updates/${telegramClientConfig.telegramBotToken}"
        logInfo { "Setting webhook. Url: $webhookUrl" }

        val setWebhookRequest = SetWebhookRequest(webhookUrl, listOf("message"))
        val telegramResponse = telegramClient.setWebhook(setWebhookRequest)
        logInfo { "Webhook was successfully set. Response: $telegramResponse" }
    }

    fun processUpdates(request: UpdatesRequest) {
        val sendMessageRequest = SendMessageRequest(request.message!!.chat!!.id, request.message!!.text)
        logInfo { "Sending message. Request: $sendMessageRequest" }

//        val telegramResponse = telegramClient.sendMessage(sendMessageRequest)
//        logInfo { "Message was successfully sent. Response: $telegramResponse" }
    }
}
