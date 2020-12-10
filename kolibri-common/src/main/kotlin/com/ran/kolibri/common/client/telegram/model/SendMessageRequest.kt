package com.ran.kolibri.common.client.telegram.model

data class SendMessageRequest(
    var chatId: Int? = null,
    var text: String? = null
)
