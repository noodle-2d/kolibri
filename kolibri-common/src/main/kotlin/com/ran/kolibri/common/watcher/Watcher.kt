package com.ran.kolibri.common.watcher

import org.joda.time.DateTime

interface Watcher {

    fun nextActionTime(): DateTime

    suspend fun doAction(): DateTime

    fun name(): String = this.javaClass.name
}
