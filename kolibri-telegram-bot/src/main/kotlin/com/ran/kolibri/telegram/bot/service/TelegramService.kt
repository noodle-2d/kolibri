package com.ran.kolibri.telegram.bot.service

import com.ran.kolibri.common.telegram.client.TelegramClient
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

    @Value("\${telegram.bot.webhook.url}")
    private lateinit var webhookUrl: String
    @Value("\${telegram.bot.owner.id}")
    private lateinit var ownerId: String

    fun setWebhook() {
        val setWebhookRequest = SetWebhookRequest(webhookUrl, listOf("message"))
        telegramClient.setWebhook(setWebhookRequest)
        logInfo { "Webhook was successfully set" }
    }

    fun processUpdates(request: UpdatesRequest) {
        val sendMessageRequest = SendMessageRequest(request.message!!.chat!!.id, request.message!!.text)
        telegramClient.sendMessage(sendMessageRequest)
    }
}
