package com.ran.kolibri.api.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.api.kodein.kafkaModule as apiKafkaModule
import com.ran.kolibri.api.kodein.managerModule
import com.ran.kolibri.api.kodein.restModule
import com.ran.kolibri.api.rest.AccountController
import com.ran.kolibri.api.rest.ExperimentController
import com.ran.kolibri.api.rest.TelegramController
import com.ran.kolibri.common.kodein.daoModule
import com.ran.kolibri.common.kodein.httpClientModule
import com.ran.kolibri.common.kodein.kafkaModule
import com.ran.kolibri.common.kodein.telegramClientModule
import com.ran.kolibri.common.rest.RestController
import com.ran.kolibri.common.starter.BaseStarter
import com.ran.kolibri.common.starter.RestApiStarter

class KolibriApiStarter : BaseStarter(), RestApiStarter {

    override fun getKodeinModules(): List<Kodein.Module> =
        listOf(
            httpClientModule,
            telegramClientModule,
            daoModule,
            kafkaModule,
            managerModule,
            restModule,
            apiKafkaModule
        )

    override fun getRestControllers(kodein: Kodein): List<RestController> =
        listOf(
            kodein.instance<TelegramController>(),
            kodein.instance<AccountController>(),
            kodein.instance<ExperimentController>()
        )
}
