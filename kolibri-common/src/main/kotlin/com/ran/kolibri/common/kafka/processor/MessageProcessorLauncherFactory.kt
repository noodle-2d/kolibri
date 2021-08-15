package com.ran.kolibri.common.kafka.processor

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.kafka.BytesKafkaConsumer
import com.ran.kolibri.common.kafka.KafkaConfig
import com.ran.kolibri.common.kafka.KafkaProcessorConfig
import com.ran.kolibri.common.kafka.serializer.JsonSerializer
import com.ran.kolibri.common.util.log
import com.ran.kolibri.common.util.runIO
import java.lang.IllegalArgumentException
import java.time.Duration
import org.apache.kafka.common.serialization.ByteArrayDeserializer

fun <M> buildJsonMessageProcessorLauncher(
    kodein: Kodein,
    processorId: String,
    klass: Class<M>,
    processor: MessageProcessor<M>
): MessageProcessorLauncher {
    val kafkaConfig = kodein.instance<KafkaConfig>()
    val kafkaProcessorConfig = kafkaConfig.processors[processorId]
        ?: throw IllegalArgumentException("Not found processor config for $processorId")
    val kafkaConsumer = buildBytesKafkaConsumer(kafkaConfig, kafkaProcessorConfig)
    val serializer = JsonSerializer(klass)

    return object : MessageProcessorLauncher {
        override suspend fun launchMessageProcessing() {
            runIO { kafkaConsumer.subscribe(listOf(kafkaProcessorConfig.topic)) }

            while (true) {
                val records = runIO { kafkaConsumer.poll(Duration.ofMillis(100)) }
                records.forEach { record ->
                    val message = serializer.deserialize(record.value())
                    var recordProcessed = false
                    while (!recordProcessed) {
                        try {
                            processor.process(message)
                            recordProcessed = true
                        } catch (e: Throwable) {
                            log.error("Error processing $message for processor $processorId", e)
                        }
                    }
                }
                runIO { kafkaConsumer.commitSync() }
            }
        }
    }
}

private fun buildBytesKafkaConsumer(
    kafkaConfig: KafkaConfig,
    kafkaProcessorConfig: KafkaProcessorConfig
): BytesKafkaConsumer {
    val properties = buildDefaultKafkaProperties(kafkaConfig, kafkaProcessorConfig)
    val bytesDeserializer = ByteArrayDeserializer()
    return BytesKafkaConsumer(properties, bytesDeserializer, bytesDeserializer)
}

private fun buildDefaultKafkaProperties(
    kafkaConfig: KafkaConfig,
    kafkaProcessorConfig: KafkaProcessorConfig
): Map<String, Any> =
    mapOf(
        "bootstrap.servers" to kafkaConfig.bootstrapServers,
        "group.id" to kafkaProcessorConfig.consumerGroup,
        "enable.auto.commit" to false
    )
