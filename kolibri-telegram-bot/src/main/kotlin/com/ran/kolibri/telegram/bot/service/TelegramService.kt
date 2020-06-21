package com.ran.kolibri.telegram.bot.service

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.util.log
import com.ran.kolibri.telegram.bot.client.TelegramClient
import com.ran.kolibri.telegram.bot.dto.config.TelegramConfig
import com.ran.kolibri.telegram.bot.dto.telegram.SendMessageRequest
import com.ran.kolibri.telegram.bot.dto.telegram.SetWebhookRequest
import com.ran.kolibri.telegram.bot.dto.updates.UpdatesRequest
import com.ran.kolibri.telegram.bot.dto.utils.SendMessageBotRequest
import com.ran.kolibri.telegram.bot.dto.utils.SendMessageToOwnerBotRequest
import com.ran.kolibri.telegram.bot.dto.utils.SetWebhookBotRequest

class TelegramService(kodein: Kodein) {

    private val telegramClient: TelegramClient = kodein.instance()
    private val telegramConfig: TelegramConfig = kodein.instance()

    suspend fun processUpdates(request: UpdatesRequest) {
        log.info("Processing updates request: $request")
        callClientToSendMessage(request.message!!.chat!!.id!!, request.message!!.text!!)
    }

    suspend fun setWebhook(request: SetWebhookBotRequest) {
        val webhookUrl = request.webhookUrl ?: "${telegramConfig.botUrl}/api/1.x/updates/${telegramConfig.botToken}"
        log.info("Setting webhook. Url: $webhookUrl")

        val setWebhookRequest = SetWebhookRequest(webhookUrl, listOf("message"))
        val telegramResponse = telegramClient.setWebhook(setWebhookRequest)
        log.info("Webhook was successfully set. Response: $telegramResponse")
    }

    suspend fun sendMessage(request: SendMessageBotRequest) {
        log.info("Processing send message request: $request")
        callClientToSendMessage(request.chatId!!, request.text!!)
    }

    suspend fun sendMessageToOwner(request: SendMessageToOwnerBotRequest) {
        log.info("Processing send message to owner request: $request")
        callClientToSendMessage(telegramConfig.botOwnerId, request.text!!)
    }

    private suspend fun callClientToSendMessage(chatId: Int, text: String) {
        val sendMessageRequest = SendMessageRequest(chatId, text)
        log.info("Sending message. Request: $sendMessageRequest")

        val telegramResponse = telegramClient.sendMessage(sendMessageRequest)
        log.info("Message was successfully sent. Response: $telegramResponse")
    }
}
