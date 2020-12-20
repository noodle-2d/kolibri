package com.ran.kolibri.api.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.api.manager.AccountManager
import com.ran.kolibri.api.rest.AccountController
import com.ran.kolibri.api.rest.TelegramController
import com.ran.kolibri.common.manager.TelegramManager

val managerModule = Kodein.Module {
    bind<TelegramManager>() with provider { TelegramManager(kodein) }
    bind<AccountManager>() with provider { AccountManager(kodein) }
}

val restModule = Kodein.Module {
    bind<TelegramController>() with provider { TelegramController(kodein) }
    bind<AccountController>() with provider { AccountController(kodein) }
}
