package com.ran.kolibri.commandline.utility.listener

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.commandline.utility.dto.action.Action
import com.ran.kolibri.commandline.utility.dto.action.ImportOldSheets
import com.ran.kolibri.commandline.utility.dto.action.Unknown
import com.ran.kolibri.commandline.utility.service.ImportOldSheetsService
import com.ran.kolibri.common.listener.StartupListener
import java.lang.IllegalArgumentException

class ActionListener(private val kodein: Kodein) : StartupListener {

    private val action: Action = kodein.instance()

    override suspend fun processStartup() =
        when (action) {
            is ImportOldSheets -> convertOldSheets()
            is Unknown -> throw IllegalArgumentException("Unknown action ${action.name}")
        }

    private suspend fun convertOldSheets() =
        kodein.instance<ImportOldSheetsService>().convertOldSheets()
}
