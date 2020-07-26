package com.ran.kolibri.telegram.bot.rest

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.ok.OkResponse
import com.ran.kolibri.common.rest.RestController
import com.ran.kolibri.telegram.bot.dto.utils.SendMessageBotRequest
import com.ran.kolibri.telegram.bot.dto.utils.SendMessageToOwnerBotRequest
import com.ran.kolibri.telegram.bot.dto.utils.SetWebhookBotRequest
import com.ran.kolibri.telegram.bot.service.TelegramService
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post

class UtilsController(kodein: Kodein) : RestController {

    private val telegramService: TelegramService = kodein.instance()

    override fun configure(route: Route): Route = route.apply {
        post("/utils/set-webhook") {
            val request: SetWebhookBotRequest = call.receive()
            telegramService.setWebhook(request)
            call.respond(OkResponse.VALUE)
        }
        post("/utils/send-message") {
            val request: SendMessageBotRequest = call.receive()
            telegramService.sendMessage(request)
            call.respond(OkResponse.VALUE)
        }
        post("/utils/send-message-to-owner") {
            val request: SendMessageToOwnerBotRequest = call.receive()
            telegramService.sendMessageToOwner(request)
            call.respond(OkResponse.VALUE)
        }
    }
}
