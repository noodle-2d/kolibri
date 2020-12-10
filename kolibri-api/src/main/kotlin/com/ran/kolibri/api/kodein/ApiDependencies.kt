package com.ran.kolibri.api.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.api.rest.TelegramController

val apiRestModule = Kodein.Module {
    bind<TelegramController>() with provider { TelegramController(kodein) }
}
