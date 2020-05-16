package com.ran.kolibri.telegram.bot.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.kodein.httpClientModule
import com.ran.kolibri.common.kodein.telegramClientModule
import com.ran.kolibri.common.rest.RestController
import com.ran.kolibri.common.starter.BaseStarter
import com.ran.kolibri.common.starter.RestApiStarter
import com.ran.kolibri.telegram.bot.kodein.telegramBotRestModule
import com.ran.kolibri.telegram.bot.kodein.telegramBotServiceModule
import com.ran.kolibri.telegram.bot.rest.UpdatesController

class TelegramBotStarter : BaseStarter(), RestApiStarter {

    override fun getKodeinModules(): List<Kodein.Module> =
        listOf(
            httpClientModule,
            telegramClientModule,
            telegramBotRestModule,
            telegramBotServiceModule
        )

    override fun getRestControllers(kodein: Kodein): List<RestController> =
        listOf(
            kodein.instance<UpdatesController>()
        )
}
