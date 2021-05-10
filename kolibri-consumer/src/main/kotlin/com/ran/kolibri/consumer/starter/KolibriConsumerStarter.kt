package com.ran.kolibri.consumer.starter

import com.github.salomonbrys.kodein.Kodein
import com.ran.kolibri.common.kodein.httpClientModule
import com.ran.kolibri.common.kodein.telegramClientModule
import com.ran.kolibri.common.starter.BaseStarter
import com.ran.kolibri.common.starter.ConsumerStarter

class KolibriConsumerStarter : BaseStarter(), ConsumerStarter {

    override fun getKodeinModules(): List<Kodein.Module> =
        listOf(
            httpClientModule,
            telegramClientModule
        )
}
