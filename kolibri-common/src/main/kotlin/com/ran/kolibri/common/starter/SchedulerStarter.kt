package com.ran.kolibri.common.starter

import com.github.salomonbrys.kodein.Kodein
import com.ran.kolibri.common.util.log
import com.ran.kolibri.common.watcher.Watcher
import kotlin.math.max
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.joda.time.DateTime

interface SchedulerStarter {

    fun getWatchers(kodein: Kodein): List<Watcher>

    suspend fun startWatchers(kodein: Kodein) = coroutineScope {
        getWatchers(kodein).forEach { watcher ->
            async {
                startWatcher(watcher)
            }
        }
    }

    private suspend fun startWatcher(watcher: Watcher) {
        val firstActionTime = watcher.nextActionTime()
        log.info("Watcher ${watcher.name()} will be executed first time at $firstActionTime")
        delayUntil(firstActionTime)

        while (true) {
            val chosenNextActionTime = try {
                watcher.doAction()
            } catch (e: Throwable) {
                val nextActionTime = DateTime.now().plusMinutes(1)
                log.error("Watcher ${watcher.name()} failed with error, next action time is $nextActionTime", e)
                nextActionTime
            }
            delayUntil(chosenNextActionTime)
        }
    }

    private suspend fun delayUntil(time: DateTime) {
        val now = DateTime.now()
        val millisUntil = max(time.millis - now.millis, 0)
        delay(millisUntil)
    }
}
