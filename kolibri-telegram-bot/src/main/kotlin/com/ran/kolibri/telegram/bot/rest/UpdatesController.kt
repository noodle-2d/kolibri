package com.ran.kolibri.telegram.bot.rest

import com.ran.kolibri.common.util.logInfo
import com.ran.kolibri.telegram.bot.dto.ok.OkResponse
import com.ran.kolibri.telegram.bot.dto.updates.UpdatesRequest
import com.ran.kolibri.telegram.bot.service.TelegramService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/updates")
class UpdatesController {

    @Autowired
    private lateinit var telegramService: TelegramService

    @RequestMapping(method = [POST], path = ["/{token}"])
    fun postUpdates(
            @RequestBody request: UpdatesRequest,
            @PathVariable("token") token: String
    ): OkResponse {
        logInfo { "Processing updates request $request for token $token" }
        telegramService.processUpdates(request)
        return OkResponse.VALUE
    }
}
