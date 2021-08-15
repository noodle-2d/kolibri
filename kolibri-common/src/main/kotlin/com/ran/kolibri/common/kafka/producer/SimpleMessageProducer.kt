package com.ran.kolibri.common.kafka.producer

import com.ran.kolibri.common.kafka.BytesKafkaProducer
import com.ran.kolibri.common.kafka.BytesProducerRecord
import com.ran.kolibri.common.kafka.serializer.Serializer
import com.ran.kolibri.common.util.runFuture

class SimpleMessageProducer<M>(
    private val kafkaProducer: BytesKafkaProducer,
    private val topic: String,
    private val messageSerializer: Serializer<M>
) : MessageProducer<M> {

    override suspend fun produce(message: M) {
        val messageBytes = messageSerializer.serialize(message)
        val record = BytesProducerRecord(topic, EMTPY_KEY, messageBytes)
        runFuture { kafkaProducer.send(record) }
    }

    companion object {
        private val EMTPY_KEY = ByteArray(0)
    }
}
