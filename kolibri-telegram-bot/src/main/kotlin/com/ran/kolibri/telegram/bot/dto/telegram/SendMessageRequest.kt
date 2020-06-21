package com.ran.kolibri.telegram.bot.dto.telegram

data class SendMessageRequest(
    var chatId: Int? = null,
    var text: String? = null
)
