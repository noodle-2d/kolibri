package com.ran.kolibri.commandline.utility.service

import com.github.salomonbrys.kodein.Kodein
import com.ran.kolibri.common.util.logInfo

class ImportOldSheetsService(kodein: Kodein) {

    suspend fun convertOldSheets() {
        logInfo("Started to import old sheets")
        // todo
    }
}
