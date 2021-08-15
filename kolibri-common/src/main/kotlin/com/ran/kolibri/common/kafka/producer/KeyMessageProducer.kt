package com.ran.kolibri.common.kafka.producer

interface KeyMessageProducer<K, M> {
    suspend fun produce(key: K, message: M)
}
