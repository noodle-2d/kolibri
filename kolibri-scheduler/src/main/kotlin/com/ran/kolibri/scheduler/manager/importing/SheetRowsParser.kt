package com.ran.kolibri.scheduler.manager.importing

import com.ran.kolibri.common.client.sheets.model.SheetRow
import com.ran.kolibri.scheduler.manager.importing.model.AccountSheetRow
import com.ran.kolibri.scheduler.manager.importing.model.FinancialAssetSheetRow
import com.ran.kolibri.scheduler.manager.importing.model.TransactionSheetRow
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object SheetRowsParser : ConverterUtils {

    fun parseFinancialAsset(row: SheetRow): FinancialAssetSheetRow =
        FinancialAssetSheetRow(
            name = row.values.first(),
            currencyString = row.values[6],
            ticker = row.values.getOrNull(7)
        )

    fun parseAccount(row: SheetRow): AccountSheetRow {
        val name = row.values.first()
        val finalAmount = row.values[1]
            .takeIf { it != "---" }
            ?.let { bigDecimal(it) }
        val isClosed = finalAmount == null
        return AccountSheetRow(name, finalAmount, isClosed)
    }

    fun parseTransaction(row: SheetRow): TransactionSheetRow {
        val accountName = row.values.first()
        val resultAmount = bigDecimal(row.values[2])
        val amount = row.values[1]
            .takeIf { it.isNotBlank() }
            ?.let { bigDecimal(it) }
            ?: resultAmount
        val comment = row.values[3]
        val date = DateTime.parse(row.values[4], DATE_FORMATTER)
        return TransactionSheetRow(accountName, amount, resultAmount, comment, date)
    }

    private val DATE_FORMATTER = DateTimeFormat.forPattern("dd.MM.yy")
}
