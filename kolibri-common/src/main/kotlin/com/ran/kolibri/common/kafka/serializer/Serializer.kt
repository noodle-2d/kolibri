package com.ran.kolibri.common.kafka.serializer

interface Serializer<T> {
    fun serialize(value: T): ByteArray
    fun deserialize(bytes: ByteArray): T
}
