package com.ran.kolibri.common.client.telegram.model

interface EditMessageRequest

data class EditTextMessageRequest(
    var chatId: Int = 0,
    var messageId: Int = 0,
    var text: String = ""
) : EditMessageRequest

data class EditButtonMessageRequest(
    var chatId: Int = 0,
    var messageId: Int = 0,
    var text: String = "",
    var replyMarkup: ReplyMarkup? = null
) : EditMessageRequest
