package com.ran.kolibri.telegram.bot.service

import com.ran.kolibri.common.telegram.client.TelegramClient
import com.ran.kolibri.common.telegram.dto.SendMessageRequest
import com.ran.kolibri.telegram.bot.dto.updates.UpdatesRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UpdatesService {

    @Autowired
    private lateinit var telegramClient: TelegramClient

    fun processUpdates(request: UpdatesRequest) {
        val sendMessageRequest = SendMessageRequest(request.message!!.chat!!.id, request.message!!.text)
        telegramClient.sendMessage(sendMessageRequest)
    }
}
