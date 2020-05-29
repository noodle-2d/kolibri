package com.ran.kolibri.commandline.utility.dto.action

import com.ran.kolibri.common.util.tryGetString
import com.typesafe.config.Config

sealed class Action {
    companion object {
        fun build(config: Config): Action =
            when (val name = config.tryGetString("action")) {
                "import-old-sheets" -> ImportOldSheets
                "set-webhook" -> SetWebhook
                else -> Unknown(name)
            }
    }
}

object ImportOldSheets : Action()

object SetWebhook : Action()

data class Unknown(val name: String?) : Action()
