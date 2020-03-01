package com.ran.kolibri.common.telegram.dto

data class SendMessageRequest(
        var chatId: Int? = null,
        var text: String? = null
)
