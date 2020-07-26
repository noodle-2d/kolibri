package com.ran.kolibri.commandline.utility.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.commandline.utility.kodein.commandLineUtilityConfigModule
import com.ran.kolibri.commandline.utility.kodein.commandLineUtilityListenerModule
import com.ran.kolibri.commandline.utility.kodein.commandLineUtilityServiceModule
import com.ran.kolibri.commandline.utility.listener.ActionListener
import com.ran.kolibri.common.kodein.daoModule
import com.ran.kolibri.common.kodein.httpClientModule
import com.ran.kolibri.common.kodein.sheetsModule
import com.ran.kolibri.common.kodein.telegramBotClientModule
import com.ran.kolibri.common.listener.StartupListener
import com.ran.kolibri.common.starter.BaseStarter
import com.ran.kolibri.common.starter.ListenerStarter

class CommandLineUtilityStarter : BaseStarter(), ListenerStarter {

    override fun getKodeinModules(): List<Kodein.Module> =
        listOf(
            httpClientModule,
            daoModule,
            sheetsModule,
            telegramBotClientModule,
            commandLineUtilityConfigModule,
            commandLineUtilityListenerModule,
            commandLineUtilityServiceModule
        )

    override fun getStartupListeners(kodein: Kodein): List<StartupListener> =
        listOf(
            kodein.instance<ActionListener>()
        )
}
