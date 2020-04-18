package com.ran.kolibri.common.starter

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.config.ServerConfig
import com.ran.kolibri.common.rest.PingController
import com.ran.kolibri.common.rest.RestController
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.coroutineScope

interface RestApiStarter {

    fun getRestControllers(kodein: Kodein): List<RestController>

    suspend fun startRestApi(kodein: Kodein) = coroutineScope {
        val serverConfig: ServerConfig = kodein.instance()

        val pingController = PingController()
        val restControllers = listOf(pingController, *getRestControllers(kodein).toTypedArray())

        embeddedServer(Netty, serverConfig.port) {
            install(DefaultHeaders)
            install(CallLogging)

            install(ContentNegotiation) {
                jackson {
                    propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
                    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }

            install(Routing) {
                route("/api/1.x") {
                    restControllers.forEach { it.configure(this) }
                }
            }
        }.start()
    }
}
