package com.ran.kolibri.telegram.bot.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.kodein.httpClientModule
import com.ran.kolibri.common.kodein.telegramClientModule
import com.ran.kolibri.common.listener.StartupListener
import com.ran.kolibri.common.rest.RestController
import com.ran.kolibri.common.starter.BaseStarter
import com.ran.kolibri.common.starter.ListenerStarter
import com.ran.kolibri.common.starter.RestApiStarter
import com.ran.kolibri.telegram.bot.kodein.telegramBotConfigModule
import com.ran.kolibri.telegram.bot.kodein.telegramBotListenerModule
import com.ran.kolibri.telegram.bot.kodein.telegramBotRestModule
import com.ran.kolibri.telegram.bot.kodein.telegramBotServiceModule
import com.ran.kolibri.telegram.bot.listener.SetWebhookListener
import com.ran.kolibri.telegram.bot.rest.UpdatesController

class TelegramBotStarter : BaseStarter(), ListenerStarter, RestApiStarter {

    override fun getKodeinModules(): List<Kodein.Module> =
            listOf(
                    httpClientModule,
                    telegramClientModule,
                    telegramBotConfigModule,
                    telegramBotRestModule,
                    telegramBotListenerModule,
                    telegramBotServiceModule
            )

    override fun getStartupListeners(kodein: Kodein): List<StartupListener> =
            listOf(
                    kodein.instance<SetWebhookListener>()
            )

    override fun getRestControllers(kodein: Kodein): List<RestController> =
            listOf(
                    kodein.instance<UpdatesController>()
            )
}
