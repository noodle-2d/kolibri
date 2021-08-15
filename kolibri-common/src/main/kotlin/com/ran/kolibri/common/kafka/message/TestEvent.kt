package com.ran.kolibri.common.kafka.message

import org.joda.time.DateTime

data class TestEvent(
    var value: String? = null,
    var number: Long? = null,
    var timestamp: DateTime? = null
)
