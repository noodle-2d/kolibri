package com.ran.kolibri.common.dto.telegram

data class SendMessageRequest(
        var chatId: Int? = null,
        var text: String? = null
)
