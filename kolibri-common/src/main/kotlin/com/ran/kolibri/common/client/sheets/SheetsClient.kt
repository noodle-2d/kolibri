package com.ran.kolibri.common.client.sheets

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.ValueRange
import com.ran.kolibri.common.client.sheets.model.SheetRange
import com.ran.kolibri.common.client.sheets.model.SheetRow
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

    suspend fun updateRange(spreadsheetId: String, range: String, values: SheetRange): Unit =
        runIO {
            sheets.spreadsheets()
                .values()
                .update(spreadsheetId, range, asValueRange(values))
                .setValueInputOption("USER_ENTERED")
                .execute()
        }

    suspend fun appendRange(spreadsheetId: String, range: String, values: SheetRange): Unit =
        runIO {
            sheets.spreadsheets()
                .values()
                .append(spreadsheetId, range, asValueRange(values))
                .setValueInputOption("USER_ENTERED")
                .execute()
        }

    private fun asValueRange(sheetRange: SheetRange): ValueRange {
        val values = sheetRange.rows.map { it.values }
        return ValueRange().setValues(values)
    }
}
