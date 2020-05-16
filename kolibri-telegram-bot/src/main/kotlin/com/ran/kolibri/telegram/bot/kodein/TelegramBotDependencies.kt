package com.ran.kolibri.telegram.bot.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.telegram.bot.rest.UpdatesController
import com.ran.kolibri.telegram.bot.service.TelegramService

val telegramBotRestModule = Kodein.Module {
    bind<UpdatesController>() with provider { UpdatesController(kodein) }
}

val telegramBotServiceModule = Kodein.Module {
    bind<TelegramService>() with provider { TelegramService(kodein) }
}
