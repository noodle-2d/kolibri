package com.ran.kolibri.common.kafka.message

data class ComplicatedTestEvent(
    val id: String,
    val events: List<TestEvent>
)
