package com.ran.kolibri.common.kafka.producer

import com.ran.kolibri.common.kafka.BytesKafkaProducer
import com.ran.kolibri.common.kafka.BytesProducerRecord
import com.ran.kolibri.common.kafka.serializer.Serializer
import com.ran.kolibri.common.util.runFuture

class KeyExtractMessageProducer<K, M>(
    private val kafkaProducer: BytesKafkaProducer,
    private val topic: String,
    private val keyExtractor: (M) -> K,
    private val keySerializer: Serializer<K>,
    private val messageSerializer: Serializer<M>
) : MessageProducer<M> {

    override suspend fun produce(message: M) {
        val key = keyExtractor(message)
        val keyBytes = keySerializer.serialize(key)
        val messageBytes = messageSerializer.serialize(message)
        val record = BytesProducerRecord(topic, keyBytes, messageBytes)
        runFuture { kafkaProducer.send(record) }
    }
}
