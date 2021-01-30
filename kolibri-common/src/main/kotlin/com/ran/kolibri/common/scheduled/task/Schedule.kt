package com.ran.kolibri.common.scheduled.task

import org.joda.time.DateTime

interface Schedule {
    fun nextTime(): DateTime
}

class EveryMilliseconds(private val millisecondsInPeriod: Int) : Schedule {

    override fun nextTime(): DateTime =
        DateTime.now().plusMillis(millisecondsInPeriod)
}

class EverySeconds(private val secondsInPeriod: Int) : Schedule {

    override fun nextTime(): DateTime =
        DateTime.now().plusSeconds(secondsInPeriod)
}

class EveryMinutes(private val minutesInPeriod: Int) : Schedule {

    override fun nextTime(): DateTime =
        DateTime.now().plusMinutes(minutesInPeriod)
}

class AtFixedHour(private val hour: Int) : Schedule {

    init {
        require(hour in 0..23)
    }

    override fun nextTime(): DateTime {
        val withFixedHour = DateTime.now()
            .withTimeAtStartOfDay()
            .withHourOfDay(hour)
        return if (withFixedHour.isBeforeNow) withFixedHour.plusDays(1) else withFixedHour
    }
}
