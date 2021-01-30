package com.ran.kolibri.scheduler.scheduled.task

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.scheduled.task.EveryMilliseconds
import com.ran.kolibri.common.scheduled.task.Schedule
import com.ran.kolibri.common.scheduled.task.ScheduledTask
import com.ran.kolibri.scheduler.manager.TelegramUpdatesManager

class TelegramPullTask(kodein: Kodein) : ScheduledTask {

    private val schedule = EveryMilliseconds(1500)
    private val telegramUpdatesManager: TelegramUpdatesManager = kodein.instance()

    override fun schedule(): Schedule = schedule
    override suspend fun doAction() = telegramUpdatesManager.pullUpdates()
}
