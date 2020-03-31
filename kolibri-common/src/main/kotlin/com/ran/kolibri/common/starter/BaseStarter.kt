package com.ran.kolibri.common.starter

import com.github.salomonbrys.kodein.Kodein
import com.ran.kolibri.common.kodein.buildConfigModule
import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.runBlocking

abstract class BaseStarter {

    abstract fun getKodeinModules(): List<Kodein.Module>

    fun startApplication() = runBlocking {
        val config = ConfigFactory.load()
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
    }
}
