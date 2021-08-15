package com.ran.kolibri.common.kafka.serializer

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule

class JsonSerializer<T>(private val klass: Class<T>) : Serializer<T> {

    override fun serialize(value: T): ByteArray =
        JSON_MAPPER
            .writeValueAsString(value)
            .let { StringSerializer.serialize(it) }

    override fun deserialize(bytes: ByteArray): T =
        StringSerializer
            .deserialize(bytes)
            .let { JSON_MAPPER.readValue(it, klass) }

    companion object {
        private val JSON_MAPPER = ObjectMapper().apply {
            registerModule(JodaModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}
