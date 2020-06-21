package com.ran.kolibri.telegram.bot.dto.telegram

data class SetWebhookRequest(
    var url: String? = null,
    var allowedUpdates: List<String>? = null
)
