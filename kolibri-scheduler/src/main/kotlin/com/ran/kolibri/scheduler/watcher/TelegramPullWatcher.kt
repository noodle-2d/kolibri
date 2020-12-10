package com.ran.kolibri.scheduler.watcher

import com.ran.kolibri.common.util.log
import com.ran.kolibri.common.watcher.Watcher
import com.ran.kolibri.common.watcher.nextTimeForEverySeconds
import org.joda.time.DateTime

class TelegramPullWatcher : Watcher {

    override fun nextActionTime(): DateTime =
        nextTimeForEverySeconds(5)

    override suspend fun doAction(): DateTime {
        log.info("Started telegram pull at ${DateTime.now()}")
        return nextActionTime()
    }
}
