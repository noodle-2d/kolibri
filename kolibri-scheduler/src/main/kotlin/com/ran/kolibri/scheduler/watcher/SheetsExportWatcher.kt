package com.ran.kolibri.scheduler.watcher

import com.ran.kolibri.common.util.log
import com.ran.kolibri.common.watcher.Watcher
import com.ran.kolibri.common.watcher.nextTimeAtFixedHour
import org.joda.time.DateTime

class SheetsExportWatcher : Watcher {

    override fun nextActionTime(): DateTime =
        nextTimeAtFixedHour(0) // 03:00 at Moscow time

    override suspend fun doAction(): DateTime {
        log.info("Started sheets export at ${DateTime.now()}")
        return nextActionTime()
    }
}
