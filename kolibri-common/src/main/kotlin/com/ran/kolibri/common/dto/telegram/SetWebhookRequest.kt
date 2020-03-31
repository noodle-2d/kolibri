package com.ran.kolibri.common.dto.telegram

data class SetWebhookRequest(
        var url: String? = null,
        var allowedUpdates: List<String>? = null
)
