package com.ran.kolibri.telegram.bot.rest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMethod.*

@RestController
@RequestMapping("/ping")
class PingController {

    @RequestMapping(method = [GET])
    fun ping(): String = "Большой босс задеплоил"
}
