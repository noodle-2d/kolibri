package com.ran.kolibri.telegram.bot.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.telegram.bot.dto.config.TelegramBotConfig
import com.ran.kolibri.telegram.bot.listener.SetWebhookListener
import com.ran.kolibri.telegram.bot.rest.UpdatesController
import com.ran.kolibri.telegram.bot.service.TelegramService

val telegramBotConfigModule = Kodein.Module {
    bind<TelegramBotConfig>() with provider { TelegramBotConfig(kodein.instance()) }
}

val telegramBotRestModule = Kodein.Module {
    bind<UpdatesController>() with provider { UpdatesController(kodein) }
}

val telegramBotListenerModule = Kodein.Module {
    bind<SetWebhookListener>() with provider { SetWebhookListener(kodein) }
}

val telegramBotServiceModule = Kodein.Module {
    bind<TelegramService>() with provider { TelegramService(kodein) }
}
