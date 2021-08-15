package com.ran.kolibri.common.kafka.message

import org.joda.time.DateTime

data class TestEvent(
    val value: String,
    val number: Long,
    val timestamp: DateTime
)
