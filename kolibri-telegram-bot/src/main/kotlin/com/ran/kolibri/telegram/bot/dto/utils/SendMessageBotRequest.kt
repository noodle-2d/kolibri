package com.ran.kolibri.telegram.bot.dto.utils

data class SendMessageBotRequest(
    var chatId: Int? = null,
    var text: String? = null
)
