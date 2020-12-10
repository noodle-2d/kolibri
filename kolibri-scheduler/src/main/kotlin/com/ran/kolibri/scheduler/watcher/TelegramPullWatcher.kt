package com.ran.kolibri.scheduler.watcher

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.watcher.Watcher
import com.ran.kolibri.common.watcher.nextTimeForEverySeconds
import com.ran.kolibri.scheduler.manager.TelegramUpdatesManager
import org.joda.time.DateTime

class TelegramPullWatcher(kodein: Kodein) : Watcher {

    private val telegramUpdatesManager: TelegramUpdatesManager = kodein.instance()

    override fun nextActionTime(): DateTime =
        nextTimeForEverySeconds(3)

    override suspend fun doAction(): DateTime {
        telegramUpdatesManager.pullUpdates()
        return nextActionTime()
    }
}
