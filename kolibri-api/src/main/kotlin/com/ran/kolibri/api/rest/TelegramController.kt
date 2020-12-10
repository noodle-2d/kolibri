package com.ran.kolibri.api.rest

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.ok.OkResponse
import com.ran.kolibri.common.dto.telegram.bot.utils.SendMessageToOwnerBotRequest
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.common.rest.RestController
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post

class TelegramController(kodein: Kodein) : RestController {

    private val telegramManager: TelegramManager = kodein.instance()

    override fun configure(route: Route): Route = route.apply {
        post("/telegram/send-message-to-owner") {
            val request: SendMessageToOwnerBotRequest = call.receive()
            telegramManager.sendMessageToOwner(request.text!!)
            call.respond(OkResponse.VALUE)
        }
    }
}
