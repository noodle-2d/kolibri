package com.ran.kolibri.common.kafka

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

typealias BytesProducerRecord = ProducerRecord<ByteArray, ByteArray>
typealias BytesKafkaProducer = KafkaProducer<ByteArray, ByteArray>
typealias BytesKafkaConsumer = KafkaConsumer<ByteArray, ByteArray>
