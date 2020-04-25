package com.ran.kolibri.common.starter

import com.github.salomonbrys.kodein.Kodein
import com.ran.kolibri.common.listener.StartupListener

interface ListenerStarter {

    fun getStartupListeners(kodein: Kodein): List<StartupListener>

    suspend fun startListeners(kodein: Kodein) =
        getStartupListeners(kodein).forEach { it.processStartup() }
}
