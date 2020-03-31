package com.ran.kolibri.common.http

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
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        }
    }
}
