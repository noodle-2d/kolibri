package com.ran.kolibri.common.kafka

import com.ran.kolibri.common.util.getConfigMap
import com.typesafe.config.Config

data class KafkaConfig(
    val bootstrapServers: String,
    val producers: Map<String, KafkaProducerConfig>,
    val processors: Map<String, KafkaProcessorConfig>
) {
    constructor(config: Config) : this(
        config.getString("kafka.bootstrap-servers"),
        config
            .getConfigMap("kafka.producers")
            .mapValues { KafkaProducerConfig(it.value) },
        config
            .getConfigMap("kafka.processors")
            .mapValues { KafkaProcessorConfig(it.value) }
    )
}

data class KafkaProducerConfig(val topic: String) {
    constructor(config: Config) : this(
        config.getString("topic")
    )
}

data class KafkaProcessorConfig(val topic: String, val consumerGroup: String) {
    constructor(config: Config) : this(
        config.getString("topic"),
        config.getString("consumer-group")
    )
}
