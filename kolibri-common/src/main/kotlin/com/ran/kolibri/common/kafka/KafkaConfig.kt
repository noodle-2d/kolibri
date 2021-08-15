package com.ran.kolibri.common.kafka

import com.ran.kolibri.common.util.getConfigMap
import com.typesafe.config.Config

// todo: add consumers config properties here
data class KafkaConfig(val bootstrapServers: String, val producers: Map<String, KafkaProducerConfig>) {
    constructor(config: Config) : this(
        config.getString("kafka.bootstrap-servers"),
        config
            .getConfigMap("kafka.producers")
            .mapValues { KafkaProducerConfig(it.value) }
    )
}

data class KafkaProducerConfig(val topic: String) {
    constructor(config: Config) : this(
        config.getString("topic")
    )
}
