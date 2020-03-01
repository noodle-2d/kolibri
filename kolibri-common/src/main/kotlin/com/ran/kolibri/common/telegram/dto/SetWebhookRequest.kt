package com.ran.kolibri.common.telegram.dto

data class SetWebhookRequest(
        var url: String? = null,
        var allowedUpdates: List<String>? = null
)
