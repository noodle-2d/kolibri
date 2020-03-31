package com.ran.kolibri.common.rest

import io.ktor.routing.Route

interface RestController {
    fun configure(route: Route): Route
}
