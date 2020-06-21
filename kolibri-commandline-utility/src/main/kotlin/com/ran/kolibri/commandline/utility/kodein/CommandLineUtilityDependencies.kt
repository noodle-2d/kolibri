package com.ran.kolibri.commandline.utility.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.commandline.utility.dto.action.Action
import com.ran.kolibri.commandline.utility.listener.ActionListener
import com.ran.kolibri.commandline.utility.service.ImportOldSheetsService

val commandLineUtilityConfigModule = Kodein.Module {
    bind<Action>() with provider { Action.build(kodein.instance()) }
}

val commandLineUtilityListenerModule = Kodein.Module {
    bind<ActionListener>() with provider { ActionListener(kodein) }
}

val commandLineUtilityServiceModule = Kodein.Module {
    bind<ImportOldSheetsService>() with provider { ImportOldSheetsService(kodein) }
}
