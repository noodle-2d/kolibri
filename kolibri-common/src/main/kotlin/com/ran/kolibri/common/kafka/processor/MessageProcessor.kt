package com.ran.kolibri.common.kafka.processor

interface MessageProcessor<M> {
    suspend fun process(message: M)
}
