package com.ran.kolibri.common.scheduled.task

import org.joda.time.DateTime

interface Schedule {
    fun nextTime(): DateTime
}

class EverySeconds(private val secondsInPeriod: Int) : Schedule {

    init {
        require(secondsInPeriod in 1..59 && 60 % secondsInPeriod == 0)
    }

    override fun nextTime(): DateTime {
        val now = DateTime.now()
        val currentSecond = now.secondOfMinute().get()
        val periodStart = currentSecond - currentSecond % secondsInPeriod

        return now
            .withMillisOfSecond(0)
            .withSecondOfMinute(periodStart)
            .plusSeconds(secondsInPeriod)
    }
}

class EveryMinutes(private val minutesInPeriod: Int) : Schedule {

    init {
        require(minutesInPeriod in 1..59 && 60 % minutesInPeriod == 0)
    }

    override fun nextTime(): DateTime {
        val now = DateTime.now()
        val currentMinute = now.minuteOfHour().get()
        val periodStart = currentMinute - currentMinute % currentMinute

        return now
            .withMillisOfSecond(0)
            .withSecondOfMinute(0)
            .withMinuteOfHour(periodStart)
            .plusMinutes(minutesInPeriod)
    }
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
