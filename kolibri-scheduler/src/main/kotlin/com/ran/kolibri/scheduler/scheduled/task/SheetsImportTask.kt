package com.ran.kolibri.scheduler.scheduled.task

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.scheduled.task.AtFixedHour
import com.ran.kolibri.common.scheduled.task.Schedule
import com.ran.kolibri.common.scheduled.task.ScheduledTask
import com.ran.kolibri.scheduler.manager.importing.ImportSheetsManager

class SheetsImportTask(kodein: Kodein) : ScheduledTask {

    private val schedule = AtFixedHour(23) // 02:00 at Moscow time
    private val importSheetsManager: ImportSheetsManager = kodein.instance()

    override fun schedule(): Schedule = schedule
    override suspend fun doAction() = importSheetsManager.importOldSheetsWithNotification()
}
