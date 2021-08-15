package com.ran.kolibri.api.rest

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.api.manager.ExperimentManager
import com.ran.kolibri.common.rest.RestController
import com.ran.kolibri.common.rest.model.OkResponse
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post

class ExperimentController(kodein: Kodein) : RestController {

    private val experimentManager: ExperimentManager = kodein.instance()

    override fun configure(route: Route): Route = route.apply {
        post("/experiments/test-event") {
            experimentManager.sendTestEvent()
            call.respond(OkResponse.VALUE)
        }
    }
}
