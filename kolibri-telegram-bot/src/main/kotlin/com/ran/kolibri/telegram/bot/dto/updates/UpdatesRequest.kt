package com.ran.kolibri.telegram.bot.dto.updates

data class UpdatesRequest(
        var updateId: Int? = null,
        var message: Message? = null
)

data class Message(
        var from: User? = null,
        var date: Int? = null,
        var text: String? = null
)

data class User(
        var id: Int? = null
)
