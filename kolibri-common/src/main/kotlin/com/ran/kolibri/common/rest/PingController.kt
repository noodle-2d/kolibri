package com.ran.kolibri.common.rest

import com.ran.kolibri.common.dto.ok.OkResponse
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

class PingController : RestController {

    override fun configure(route: Route) = route.apply {
        get("/ping") {
            call.respond(OkResponse.VALUE)
        }
    }
}
