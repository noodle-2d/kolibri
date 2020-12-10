package com.ran.kolibri.api.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.api.kodein.apiRestModule
import com.ran.kolibri.api.rest.TelegramController
import com.ran.kolibri.common.kodein.httpClientModule
import com.ran.kolibri.common.kodein.telegramModule
import com.ran.kolibri.common.rest.RestController
import com.ran.kolibri.common.starter.BaseStarter
import com.ran.kolibri.common.starter.RestApiStarter

class TelegramBotStarter : BaseStarter(), RestApiStarter {

    override fun getKodeinModules(): List<Kodein.Module> =
        listOf(
            httpClientModule,
            telegramModule,
            apiRestModule
        )

    override fun getRestControllers(kodein: Kodein): List<RestController> =
        listOf(
            kodein.instance<TelegramController>()
        )
}
