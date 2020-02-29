package com.ran.kolibri.telegram.bot.rest

import com.ran.kolibri.common.util.logInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMethod.*

@RestController
@RequestMapping("/ping")
class PingController {

    @RequestMapping(method = [GET])
    fun ping(): ResponseEntity<Any> {
        logInfo { "Ping invoked" }
        return ResponseEntity(HttpStatus.OK)
    }
}
