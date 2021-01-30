package com.ran.kolibri.common.client.telegram.model

data class ReplyMarkup(var inlineKeyboard: List<List<Button>>? = null)

data class Button(
    var text: String? = null,
    var callbackData: String? = null
)
