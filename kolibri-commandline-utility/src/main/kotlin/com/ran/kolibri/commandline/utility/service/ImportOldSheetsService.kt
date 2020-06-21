package com.ran.kolibri.commandline.utility.service

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.SheetsClient
import com.ran.kolibri.common.dto.config.GoogleConfig
import com.ran.kolibri.common.dto.sheets.SheetRange
import com.ran.kolibri.common.util.log

class ImportOldSheetsService(kodein: Kodein) {

    private val googleConfig: GoogleConfig = kodein.instance()
    private val sheetsClient: SheetsClient = kodein.instance()

    suspend fun convertOldSheets() {
        log.info("Started to import old sheets")

        val transactionRanges = importTransactionRanges()
        val accountsRange = importAccountsRange()
    }

    private suspend fun importTransactionRanges(): List<SheetRange> =
        TRANSACTION_RANGE_NAMES.map { importRange(it) }

    private suspend fun importAccountsRange(): SheetRange =
        importRange(ACCOUNTS_RANGE_NAME)

    private suspend fun importRange(rangeName: String): SheetRange {
        log.info("Importing range $rangeName")
        val range = sheetsClient.getRange(googleConfig.accountsSpreadsheetId, rangeName)
        log.info("Imported range $rangeName: $range")
        return range
    }

    companion object {
        private val TRANSACTION_RANGE_NAMES = (2015..2020).map { "$it!A2:F" }
        private const val ACCOUNTS_RANGE_NAME = "Счета!A2:A"
    }
}
