package com.ran.kolibri.monolite.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.api.kodein.restModule
import com.ran.kolibri.api.rest.AccountController
import com.ran.kolibri.api.rest.TelegramController
import com.ran.kolibri.common.kodein.daoModule
import com.ran.kolibri.common.kodein.httpClientModule
import com.ran.kolibri.common.kodein.openExchangeRatesClientModule
import com.ran.kolibri.common.kodein.sheetsModule
import com.ran.kolibri.common.kodein.telegramModule
import com.ran.kolibri.common.kodein.tinkoffInvestingClientModule
import com.ran.kolibri.common.rest.RestController
import com.ran.kolibri.common.scheduled.task.ScheduledTask
import com.ran.kolibri.common.starter.BaseStarter
import com.ran.kolibri.common.starter.RestApiStarter
import com.ran.kolibri.common.starter.SchedulerStarter
import com.ran.kolibri.scheduler.kodein.scheduledTaskModule
import com.ran.kolibri.scheduler.scheduled.task.CurrencyPricesUpdateTask
import com.ran.kolibri.scheduler.scheduled.task.SheetsImportTask
import com.ran.kolibri.scheduler.scheduled.task.TelegramPullTask
import com.ran.kolibri.api.kodein.managerModule as apiManagerModule
import com.ran.kolibri.scheduler.kodein.managerModule as schedulerManagerModule

class KolibriMonoliteStarter : BaseStarter(), RestApiStarter, SchedulerStarter {

    override fun getKodeinModules(): List<Kodein.Module> =
        listOf(
            httpClientModule,
            telegramModule,
            tinkoffInvestingClientModule,
            sheetsModule,
            openExchangeRatesClientModule,
            daoModule,
            schedulerManagerModule,
            scheduledTaskModule,
            apiManagerModule,
            restModule
        )

    override fun getRestControllers(kodein: Kodein): List<RestController> =
        listOf(
            kodein.instance<TelegramController>(),
            kodein.instance<AccountController>()
        )

    override fun getScheduledTasks(kodein: Kodein): List<ScheduledTask> =
        listOf(
            kodein.instance<SheetsImportTask>(),
            kodein.instance<TelegramPullTask>(),
            kodein.instance<CurrencyPricesUpdateTask>()
        )
}
