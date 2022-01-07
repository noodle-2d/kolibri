package com.ran.kolibri.common.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header

fun buildHttpClient() = HttpClient {
    defaultRequest {
        header("Content-Type", "application/json;charset=UTF-8")
    }
    install(JsonFeature) {
        serializer = JacksonSerializer {
            // todo: use something else instead of it!
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }
    }
}
