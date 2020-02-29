package com.ran.kolibri.telegram.bot.rest

import com.ran.kolibri.common.util.logInfo
import com.ran.kolibri.telegram.bot.dto.updates.UpdatesRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/updates")
class UpdatesController {

    @RequestMapping(method = [POST], path = ["/{token}"])
    fun postUpdates(
            @RequestBody request: UpdatesRequest,
            @PathVariable("token") token: String
    ): ResponseEntity<Any> {
        logInfo { "Processing updates request $request for token $token" }
        return ResponseEntity(HttpStatus.OK)
    }
}
