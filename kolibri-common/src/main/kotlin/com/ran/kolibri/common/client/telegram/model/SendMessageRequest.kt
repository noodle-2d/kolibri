package com.ran.kolibri.common.client.telegram.model

interface SendMessageRequest

data class SendTextMessageRequest(
    var chatId: Int = 0,
    var text: String = ""
) : SendMessageRequest

data class SendButtonMessageRequest(
    var chatId: Int = 0,
    var text: String = "",
    var replyMarkup: ReplyMarkup? = null
) : SendMessageRequest
