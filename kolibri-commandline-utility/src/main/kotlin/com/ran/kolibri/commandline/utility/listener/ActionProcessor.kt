package com.ran.kolibri.commandline.utility.listener

import com.ran.kolibri.commandline.utility.dto.action.ActionResult

interface ActionProcessor {
    suspend fun processAction(): ActionResult
}
