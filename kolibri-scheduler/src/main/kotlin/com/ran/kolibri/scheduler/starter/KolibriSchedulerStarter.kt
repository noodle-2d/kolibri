package com.ran.kolibri.scheduler.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.kodein.daoModule
import com.ran.kolibri.common.kodein.httpClientModule
import com.ran.kolibri.common.kodein.sheetsModule
import com.ran.kolibri.common.kodein.telegramClientModule
import com.ran.kolibri.common.starter.BaseStarter
import com.ran.kolibri.common.starter.SchedulerStarter
import com.ran.kolibri.common.watcher.Watcher
import com.ran.kolibri.scheduler.kodein.managerModule
import com.ran.kolibri.scheduler.kodein.watcherModule
import com.ran.kolibri.scheduler.watcher.OldSheetsImportWatcher
import com.ran.kolibri.scheduler.watcher.SheetsExportWatcher
import com.ran.kolibri.scheduler.watcher.TelegramPullWatcher

class KolibriSchedulerStarter : BaseStarter(), SchedulerStarter {

    override fun getKodeinModules(): List<Kodein.Module> =
        listOf(
            httpClientModule,
            telegramClientModule,
            sheetsModule,
            daoModule,
            managerModule,
            watcherModule
        )

    override fun getWatchers(kodein: Kodein): List<Watcher> =
        listOf(
            kodein.instance<OldSheetsImportWatcher>(),
            kodein.instance<SheetsExportWatcher>(),
            kodein.instance<TelegramPullWatcher>()
        )
}
