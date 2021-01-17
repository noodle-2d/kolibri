package com.ran.kolibri.common.starter

import com.github.salomonbrys.kodein.Kodein
import com.ran.kolibri.common.scheduled.task.ScheduledTask
import com.ran.kolibri.common.util.log
import kotlin.math.max
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.joda.time.DateTime

interface SchedulerStarter {

    fun getScheduledTasks(kodein: Kodein): List<ScheduledTask>

    suspend fun startScheduledTasks(kodein: Kodein) = coroutineScope {
        getScheduledTasks(kodein).forEach { watcher ->
            async {
                startScheduledTask(watcher)
            }
        }
    }

    private suspend fun startScheduledTask(scheduledTask: ScheduledTask) {
        val firstActionTime = scheduledTask.schedule().nextTime()
        log.info("Watcher ${scheduledTask.name()} will be executed first time at $firstActionTime")
        delayUntil(firstActionTime)

        while (true) {
            try {
                scheduledTask.doAction()
            } catch (e: Throwable) {
                log.error("Watcher ${scheduledTask.name()} failed with error", e)
            }
            delayUntil(scheduledTask.schedule().nextTime())
        }
    }

    private suspend fun delayUntil(time: DateTime) {
        val now = DateTime.now()
        val millisUntil = max(time.millis - now.millis, 0)
        delay(millisUntil)
    }
}
