package com.ran.kolibri.common.client.telegram.model

data class UpdatesResponse(
    var ok: Boolean? = null,
    var result: List<Update>? = null
)

data class Update(
    var updateId: Long? = null,
    var message: Message? = null,
    val callbackQuery: CallbackQuery? = null
)

data class CallbackQuery(
    var from: User? = null,
    var message: Message? = null,
    var data: String? = null
)
