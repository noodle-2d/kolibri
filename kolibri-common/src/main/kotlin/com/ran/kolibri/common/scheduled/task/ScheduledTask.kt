package com.ran.kolibri.common.scheduled.task

interface ScheduledTask {

    fun schedule(): Schedule

    suspend fun doAction()

    fun name(): String = this.javaClass.name
}
