package com.ran.kolibri.telegram.bot.rest

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.ok.OkResponse
import com.ran.kolibri.common.rest.RestController
import com.ran.kolibri.telegram.bot.dto.config.TelegramConfig
import com.ran.kolibri.telegram.bot.dto.updates.UpdatesRequest
import com.ran.kolibri.telegram.bot.service.TelegramService
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post

class UpdatesController(kodein: Kodein) : RestController {

    private val telegramService: TelegramService = kodein.instance()
    private val telegramConfig: TelegramConfig = kodein.instance()

    override fun configure(route: Route): Route = route.apply {
        post("/updates/${telegramConfig.botToken}") {
            val request: UpdatesRequest = call.receive()
            telegramService.processUpdates(request)
            call.respond(OkResponse.VALUE)
        }
    }
}
