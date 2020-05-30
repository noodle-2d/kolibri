package com.ran.kolibri.telegram.bot.service

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.telegram.SendMessageRequest
import com.ran.kolibri.common.telegram.TelegramClient
import com.ran.kolibri.common.util.log
import com.ran.kolibri.telegram.bot.dto.updates.UpdatesRequest

class TelegramService(kodein: Kodein) {

    private val telegramClient: TelegramClient = kodein.instance()

    suspend fun processUpdates(request: UpdatesRequest) {
        log.info("Processing updates request: $request")

        val sendMessageRequest = SendMessageRequest(request.message!!.chat!!.id, request.message!!.text)
        log.info("Sending message. Request: $sendMessageRequest")

        val telegramResponse = telegramClient.sendMessage(sendMessageRequest)
        log.info("Message was successfully sent. Response: $telegramResponse")
    }
}
