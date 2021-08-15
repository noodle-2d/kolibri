package com.ran.kolibri.common.kafka.producer

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.kafka.BytesKafkaProducer
import com.ran.kolibri.common.kafka.KafkaConfig
import com.ran.kolibri.common.kafka.serializer.JsonSerializer
import org.apache.kafka.common.serialization.ByteArraySerializer

fun buildBytesKafkaProducer(kodein: Kodein): BytesKafkaProducer {
    val kafkaConfig = kodein.instance<KafkaConfig>()
    val properties = buildDefaultKafkaProperties(kafkaConfig)
    val bytesSerializer = ByteArraySerializer()
    return BytesKafkaProducer(properties, bytesSerializer, bytesSerializer)
}

private fun buildDefaultKafkaProperties(kafkaConfig: KafkaConfig): Map<String, Any> =
    mapOf(
        "bootstrap.servers" to kafkaConfig.bootstrapServers,
        "acks" to "all",
        "retries" to 0,
        "linger.ms" to 100
    )

fun <T> buildJsonMessageProducer(kodein: Kodein, producerId: String, klass: Class<T>): MessageProducer<T> {
    val kafkaConfig = kodein.instance<KafkaConfig>()
    val kafkaProducerConfig = kafkaConfig.producers[producerId]
        ?: throw IllegalArgumentException("Not found producer config for $producerId")
    val kafkaProducer = kodein.instance<BytesKafkaProducer>()
    val serializer = JsonSerializer(klass)
    return SimpleMessageProducer(kafkaProducer, kafkaProducerConfig.topic, serializer)
}
