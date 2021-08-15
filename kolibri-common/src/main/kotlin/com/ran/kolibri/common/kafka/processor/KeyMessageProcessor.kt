package com.ran.kolibri.common.kafka.processor

interface KeyMessageProcessor<K, M> {
    suspend fun process(key: K, message: M)
}
