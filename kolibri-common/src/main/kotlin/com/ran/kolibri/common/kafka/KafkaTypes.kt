package com.ran.kolibri.common.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

typealias BytesKafkaProducer = KafkaProducer<ByteArray, ByteArray>
typealias BytesProducerRecord = ProducerRecord<ByteArray, ByteArray>
