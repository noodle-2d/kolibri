package com.ran.kolibri.common.kafka.serializer

object StringSerializer : Serializer<String> {

    override fun serialize(value: String): ByteArray =
        value.toByteArray()

    override fun deserialize(bytes: ByteArray): String =
        String(bytes)
}
