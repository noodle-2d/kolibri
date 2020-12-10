package com.ran.kolibri.common.watcher

import org.joda.time.DateTime

fun nextTimeForEverySeconds(secondsInPeriod: Int): DateTime {
    require(secondsInPeriod in 1..59 && 60 % secondsInPeriod == 0)

    val now = DateTime.now()
    val currentSecond = now.secondOfMinute().get()
    val periodStart = currentSecond - currentSecond % secondsInPeriod

    return now
        .withMillisOfSecond(0)
        .withSecondOfMinute(periodStart)
        .plusSeconds(secondsInPeriod)
}

fun nextTimeForEveryMinutes(minutesInPeriod: Int): DateTime {
    require(minutesInPeriod in 1..59 && 60 % minutesInPeriod == 0)

    val now = DateTime.now()
    val currentMinute = now.minuteOfHour().get()
    val periodStart = currentMinute - currentMinute % currentMinute

    return now
        .withMillisOfSecond(0)
        .withSecondOfMinute(0)
        .withMinuteOfHour(periodStart)
        .plusMinutes(minutesInPeriod)
}

fun nextTimeAtFixedHour(hour: Int): DateTime {
    require(hour in 0..23)

    val withFixedHour = DateTime.now()
        .withTimeAtStartOfDay()
        .withHourOfDay(hour)
    return if (withFixedHour.isBeforeNow) withFixedHour.plusDays(1) else withFixedHour
}
