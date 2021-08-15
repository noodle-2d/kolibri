package com.ran.kolibri.common.kafka.producer

import com.ran.kolibri.common.kafka.BytesKafkaProducer
import com.ran.kolibri.common.kafka.BytesProducerRecord
import com.ran.kolibri.common.kafka.serializer.Serializer
import com.ran.kolibri.common.util.runFuture

class SimpleKeyMessageProducer<K, M>(
    private val kafkaProducer: BytesKafkaProducer,
    private val topic: String,
    private val keySerializer: Serializer<K>,
    private val messageSerializer: Serializer<M>
) : KeyMessageProducer<K, M> {

    override suspend fun produce(key: K, message: M) {
        val keyBytes = keySerializer.serialize(key)
        val messageBytes = messageSerializer.serialize(message)
        val record = BytesProducerRecord(topic, keyBytes, messageBytes)
        runFuture { kafkaProducer.send(record) }
    }
}
