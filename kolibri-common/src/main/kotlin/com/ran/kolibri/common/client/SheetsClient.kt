package com.ran.kolibri.common.client

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.google.api.services.sheets.v4.Sheets
import com.ran.kolibri.common.dto.sheets.SheetRange
import com.ran.kolibri.common.dto.sheets.SheetRow
import com.ran.kolibri.common.util.runIO

class SheetsClient(kodein: Kodein) {

    private val sheets: Sheets = kodein.instance()

    suspend fun getRange(spreadsheetId: String, range: String): SheetRange =
        runIO {
            val response = sheets
                .spreadsheets()
                .values()[spreadsheetId, range]
                .execute()
                .getValues() ?: listOf()
            val rows = response.map { responseRow ->
                val rowValues = responseRow.map { it.toString() }
                SheetRow(rowValues)
            }
            SheetRange(rows)
        }
}
