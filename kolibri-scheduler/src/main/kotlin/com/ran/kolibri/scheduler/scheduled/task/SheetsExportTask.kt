package com.ran.kolibri.scheduler.scheduled.task

import com.ran.kolibri.common.scheduled.task.AtFixedHour
import com.ran.kolibri.common.scheduled.task.Schedule
import com.ran.kolibri.common.scheduled.task.ScheduledTask
import com.ran.kolibri.common.util.log
import org.joda.time.DateTime

class SheetsExportTask : ScheduledTask {

    private val schedule = AtFixedHour(0) // 03:00 at Moscow time

    override fun schedule(): Schedule = schedule
    override suspend fun doAction() = log.info("Started sheets export at ${DateTime.now()}")
}
