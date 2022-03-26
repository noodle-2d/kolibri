package com.ran.kolibri.scheduler.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.kodein.daoModule
import com.ran.kolibri.common.kodein.httpClientModule
import com.ran.kolibri.common.kodein.openExchangeRatesClientModule
import com.ran.kolibri.common.kodein.sheetsModule
import com.ran.kolibri.common.kodein.telegramClientModule
import com.ran.kolibri.common.kodein.tinkoffInvestingClientModule
import com.ran.kolibri.common.scheduled.task.ScheduledTask
import com.ran.kolibri.common.starter.BaseStarter
import com.ran.kolibri.common.starter.SchedulerStarter
import com.ran.kolibri.scheduler.kodein.managerModule
import com.ran.kolibri.scheduler.kodein.scheduledTaskModule
import com.ran.kolibri.scheduler.scheduled.task.CurrencyPricesUpdateTask
import com.ran.kolibri.scheduler.scheduled.task.OldSheetsImportTask
import com.ran.kolibri.scheduler.scheduled.task.TelegramPullTask

class KolibriSchedulerStarter : BaseStarter(), SchedulerStarter {

    override fun getKodeinModules(): List<Kodein.Module> =
        listOf(
            httpClientModule,
            telegramClientModule,
            tinkoffInvestingClientModule,
            sheetsModule,
            openExchangeRatesClientModule,
            daoModule,
            managerModule,
            scheduledTaskModule
        )

    override fun getScheduledTasks(kodein: Kodein): List<ScheduledTask> =
        listOf(
            kodein.instance<OldSheetsImportTask>(),
            kodein.instance<TelegramPullTask>(),
            kodein.instance<CurrencyPricesUpdateTask>()
        )
}
