package com.ran.kolibri.scheduler.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.scheduler.manager.TelegramUpdatesManager
import com.ran.kolibri.scheduler.watcher.OldSheetsImportWatcher
import com.ran.kolibri.scheduler.watcher.SheetsExportWatcher
import com.ran.kolibri.scheduler.watcher.TelegramPullWatcher

val managerModule = Kodein.Module {
    bind<TelegramManager>() with provider { TelegramManager(kodein) }
    bind<TelegramUpdatesManager>() with provider { TelegramUpdatesManager(kodein) }
}

val watcherModule = Kodein.Module {
    bind<OldSheetsImportWatcher>() with provider { OldSheetsImportWatcher() }
    bind<SheetsExportWatcher>() with provider { SheetsExportWatcher() }
    bind<TelegramPullWatcher>() with provider { TelegramPullWatcher(kodein) }
}
