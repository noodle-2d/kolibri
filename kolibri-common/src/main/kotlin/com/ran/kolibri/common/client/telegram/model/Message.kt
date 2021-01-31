package com.ran.kolibri.common.client.telegram.model

data class Message(
    var messageId: Int = 0,
    var from: User? = null,
    var date: Int? = null,
    var chat: Chat? = null,
    var text: String? = null
)

data class Chat(
    var id: Int = 0
)

data class User(
    var id: Int = 0
)
