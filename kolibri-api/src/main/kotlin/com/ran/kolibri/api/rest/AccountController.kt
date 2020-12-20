package com.ran.kolibri.api.rest

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.api.manager.AccountManager
import com.ran.kolibri.common.rest.RestController
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

class AccountController(kodein: Kodein) : RestController {

    private val accountManager: AccountManager = kodein.instance()

    override fun configure(route: Route): Route = route.apply {
        get("/accounts") {
            call.respond(accountManager.getAccounts())
        }
    }
}
