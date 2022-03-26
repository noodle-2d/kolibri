package com.ran.kolibri.common.starter

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.config.ServerConfig
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

interface RestApiStarter {

    fun getRestControllers(kodein: Kodein): List<RestController>

    fun CoroutineScope.startRestApi(kodein: Kodein) {
        val serverConfig: ServerConfig = kodein.instance()

        val pingController = PingController()
        val restControllers = listOf(pingController, *getRestControllers(kodein).toTypedArray())

        val server = embeddedServer(Netty, serverConfig.port) {
            install(DefaultHeaders)
            install(CallLogging)

            install(ContentNegotiation) {
                jackson {
                    registerModule(JodaModule())
                    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                }
            }

            install(Routing) {
                route("/api/v1") {
                    restControllers.forEach { it.configure(this) }
                }
            }
        }

        async {
            server.start()
        }
    }
}
