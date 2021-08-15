package com.ran.kolibri.common.kafka.message

data class ComplicatedTestEvent(
    var id: String? = null,
    var events: List<TestEvent>? = null
)
