package com.ran.kolibri.scheduler.manager.transaction

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.sheets.SheetsClient
import com.ran.kolibri.common.client.sheets.model.GoogleConfig
import com.ran.kolibri.common.client.sheets.model.SheetRange
import com.ran.kolibri.common.client.sheets.model.SheetRow
import com.ran.kolibri.common.manager.Processed
import com.ran.kolibri.common.manager.ProcessingInContextResult
import com.ran.kolibri.common.manager.TelegramManager

class AddFinancialAssetTransactionManager(kodein: Kodein) {

    private val telegramManager: TelegramManager = kodein.instance()

    private val googleConfig: GoogleConfig = kodein.instance()
    private val sheetsClient: SheetsClient = kodein.instance()

    suspend fun startAddingFinancialAssetTransaction(messageId: Int): ProcessingInContextResult {
        telegramManager.editMessageToOwner(messageId, "Adding financial asset transaction. (todo)")
        return Processed(null)
    }

    private suspend fun updateSheets() {
        val range = "Тестинг!A1"
        val values = SheetRange(listOf(SheetRow(listOf("120,20", "340")), SheetRow(listOf("31.01.21", "780.09"))))
        sheetsClient.updateRange(googleConfig.accountsSpreadsheetId, range, values)
        telegramManager.sendMessageToOwner("Sheets updated")
    }

    private suspend fun appendSheets() {
        val range = "Тестинг!A1"
        val values = SheetRange(listOf(SheetRow(listOf("11", "33")), SheetRow(listOf("55", "77"))))
        sheetsClient.appendRange(googleConfig.accountsSpreadsheetId, range, values)
        telegramManager.sendMessageToOwner("Sheets appended")
    }
}

data class AddFinancialAssetTransactionContext(
    override val messageId: Int
) : AddTransactionContext
