package com.ran.kolibri.common.starter

import com.github.salomonbrys.kodein.Kodein
import com.ran.kolibri.common.kodein.buildConfigModule
import com.ran.kolibri.common.util.buildConfig
import kotlinx.coroutines.runBlocking

abstract class BaseStarter {

    abstract fun getKodeinModules(): List<Kodein.Module>

    fun startApplication(args: Array<String>) = runBlocking {
        val config = buildConfig(args)
        val configModule = buildConfigModule(config)

        val kodein = Kodein {
            import(configModule)
            getKodeinModules().forEach { import(it) }
        }

        if (this@BaseStarter is ListenerStarter) {
            startListeners(kodein)
        }
        if (this@BaseStarter is RestApiStarter) {
            startRestApi(kodein)
        }
        if (this@BaseStarter is SchedulerStarter) {
            startScheduledTasks(kodein)
        }
        if (this@BaseStarter is ConsumerStarter) {
            startConsumers(kodein)
        }
    }
}
