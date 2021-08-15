package com.ran.kolibri.common.kafka.producer

interface MessageProducer<M> {
    suspend fun produce(message: M)
}
