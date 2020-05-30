package com.ran.kolibri.commandline.utility.service

import com.github.salomonbrys.kodein.Kodein
import com.ran.kolibri.common.util.log

class ImportOldSheetsService(kodein: Kodein) {

    suspend fun convertOldSheets() {
        log.info("Started to import old sheets")
        // todo
    }
}
