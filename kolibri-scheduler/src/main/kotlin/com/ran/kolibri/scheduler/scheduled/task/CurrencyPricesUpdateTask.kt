package com.ran.kolibri.scheduler.scheduled.task

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.scheduled.task.EveryMinutes
import com.ran.kolibri.common.scheduled.task.Schedule
import com.ran.kolibri.common.scheduled.task.ScheduledTask
import com.ran.kolibri.scheduler.manager.prices.CurrencyPricesManager

class CurrencyPricesUpdateTask(kodein: Kodein) : ScheduledTask {

    // private val schedule = AtFixedHour(18) // 21:00 at Moscow time
    private val schedule = EveryMinutes(1)
    private val currencyPricesManager: CurrencyPricesManager = kodein.instance()

    override fun schedule(): Schedule = schedule
    override suspend fun doAction() = currencyPricesManager.updateCurrencyPrices()
}
