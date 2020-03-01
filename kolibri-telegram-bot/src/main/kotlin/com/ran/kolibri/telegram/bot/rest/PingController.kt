package com.ran.kolibri.telegram.bot.rest

import com.ran.kolibri.common.util.logInfo
import com.ran.kolibri.telegram.bot.dto.ok.OkResponse
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMethod.*

@RestController
@RequestMapping("/ping")
class PingController {

    @RequestMapping(method = [GET])
    fun ping(): OkResponse {
        logInfo { "Ping invoked" }
        return OkResponse.VALUE
    }
}
