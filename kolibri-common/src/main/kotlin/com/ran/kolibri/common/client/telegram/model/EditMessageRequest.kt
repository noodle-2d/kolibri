package com.ran.kolibri.common.client.telegram.model

interface EditMessageRequest

data class EditTextMessageRequest(
    var chatId: Int? = null,
    var messageId: Int? = null,
    var text: String? = null
) : SendMessageRequest

data class EditButtonMessageRequest(
    var chatId: Int? = null,
    var messageId: Int? = null,
    var text: String? = null,
    var replyMarkup: ReplyMarkup? = null
) : SendMessageRequest
