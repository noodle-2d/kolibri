package com.ran.kolibri.common.client.telegram.model

data class UpdatesResponse(
    var ok: Boolean? = null,
    var result: List<Update>? = null
)

data class Update(
    var updateId: Long? = null,
    var message: Message? = null
)

data class Message(
    var from: User? = null,
    var date: Int? = null,
    var chat: Chat? = null,
    var text: String? = null
)

data class Chat(
    var id: Int? = null
)

data class User(
    var id: Int? = null
)
