package com.ran.kolibri.scheduler.watcher

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.watcher.Watcher
import com.ran.kolibri.common.watcher.nextTimeAtFixedHour
import com.ran.kolibri.scheduler.manager.importing.ImportOldSheetsManager
import org.joda.time.DateTime

class OldSheetsImportWatcher(kodein: Kodein) : Watcher {

    private val importOldSheetsManager: ImportOldSheetsManager = kodein.instance()

    override fun nextActionTime(): DateTime =
        nextTimeAtFixedHour(23) // 02:00 at Moscow time

    override suspend fun doAction(): DateTime {
        importOldSheetsManager.importOldSheets()
        return nextActionTime()
    }
}
