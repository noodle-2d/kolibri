package com.ran.kolibri.scheduler.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.starter.BaseStarter
import com.ran.kolibri.common.starter.WatcherStarter
import com.ran.kolibri.common.watcher.Watcher
import com.ran.kolibri.scheduler.kodein.watchersModule
import com.ran.kolibri.scheduler.watcher.OldSheetsImportWatcher
import com.ran.kolibri.scheduler.watcher.SheetsExportWatcher
import com.ran.kolibri.scheduler.watcher.TelegramPullWatcher

class SchedulerStarter : BaseStarter(), WatcherStarter {

    override fun getKodeinModules(): List<Kodein.Module> =
        listOf(
            watchersModule
        )

    override fun getWatchers(kodein: Kodein): List<Watcher> =
        listOf(
            kodein.instance<OldSheetsImportWatcher>(),
            kodein.instance<SheetsExportWatcher>(),
            kodein.instance<TelegramPullWatcher>()
        )
}
