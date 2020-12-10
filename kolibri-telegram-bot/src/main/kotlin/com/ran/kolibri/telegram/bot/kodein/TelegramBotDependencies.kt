package com.ran.kolibri.telegram.bot.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.telegram.bot.rest.UtilsController

val telegramBotRestModule = Kodein.Module {
    bind<UtilsController>() with provider { UtilsController(kodein) }
}
