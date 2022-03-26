package com.ran.kolibri.scheduler.manager.importing

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.sheets.SheetsClient
import com.ran.kolibri.common.client.sheets.model.GoogleConfig
import com.ran.kolibri.common.client.sheets.model.SheetRange
import com.ran.kolibri.common.dao.AccountDao
import com.ran.kolibri.common.dao.FinancialAssetDao
import com.ran.kolibri.common.dao.TransactionDao
import com.ran.kolibri.common.entity.Account
import com.ran.kolibri.common.entity.FinancialAsset
import com.ran.kolibri.common.entity.Transaction
import com.ran.kolibri.common.entity.enums.FinancialAssetType
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.common.util.log
import com.ran.kolibri.scheduler.manager.importing.model.AccountImportDto
import com.ran.kolibri.scheduler.manager.importing.model.ImportedData
import com.ran.kolibri.scheduler.manager.importing.model.TransactionImportDto
import com.ran.kolibri.scheduler.manager.telegram.SingleActionUpdateProcessor
import com.ran.kolibri.scheduler.manager.telegram.TelegramBotNotifyingUtils
import com.ran.kolibri.scheduler.manager.telegram.model.TelegramOperationType
import java.lang.IllegalArgumentException

class ImportSheetsManager(kodein: Kodein) : SingleActionUpdateProcessor, TelegramBotNotifyingUtils {

    private val googleConfig: GoogleConfig = kodein.instance()
    private val sheetsClient: SheetsClient = kodein.instance()

    private val financialAssetDao: FinancialAssetDao = kodein.instance()
    private val accountDao: AccountDao = kodein.instance()
    private val transactionDao: TransactionDao = kodein.instance()

    private val transactionEnrichManager: TransactionEnrichManager = kodein.instance()
    override val telegramManager: TelegramManager = kodein.instance()

    override val operationType: TelegramOperationType
        get() = TelegramOperationType.IMPORT_OLD_SHEETS

    override suspend fun doProcessUpdate() =
        importOldSheetsWithNotification()

    suspend fun importOldSheetsWithNotification() =
        doActionSendingMessageToOwner("importing old sheets") { importOldSheets() }

    private suspend fun importOldSheets(): String {
        log.info("Started to import old sheets")

        // todo: uncomment it when importing is refactored completely
        // val importedData = importSheetsData()

        deleteAll()
        restartSequences()

        val financialAssetsRange = importFinancialAssetsRange()
        val accountsRange = importAccountsRange()
        val transactionRanges = importTransactionRanges()

        val financialAssets = convertAndInsertFinancialAssets(financialAssetsRange)

        val accountImportDtoList = convertAccountsToImportDto(accountsRange, financialAssets)
        val transactionImportDtoList = convertTransactionsToImportDto(transactionRanges)

        TransactionsValidator.validate(transactionImportDtoList, accountImportDtoList)

        val accounts = insertAccounts(accountImportDtoList, transactionImportDtoList)
        val transactions = insertTransactions(transactionImportDtoList, accounts)

        transactionEnrichManager.enrichTransactions(transactions)

        val resultMessage = "${transactions.size} transactions, ${accounts.size} accounts and " +
            "${financialAssets.size} financial assets were successfully imported."
        log.info("Finished importing old sheets with result: $resultMessage")
        return resultMessage
    }

    private suspend fun deleteAll() {
        val deletedTransactionsCount = transactionDao.deleteAllTransactions()
        log.info("Deleted $deletedTransactionsCount transactions from database before import")

        val deletedAccountsCount = accountDao.deleteAllAccounts()
        log.info("Deleted $deletedAccountsCount accounts from database before import")

        val deletedFinancialAssetsCount = financialAssetDao.deleteAllFinancialAssets()
        log.info("Deleted $deletedFinancialAssetsCount financial assets from database before import")
    }

    private suspend fun restartSequences() {
        financialAssetDao.restartIdSequence()
        accountDao.restartIdSequence()
        transactionDao.restartIdSequence()
        log.info("Restarted sequences before import")
    }

    private suspend fun importSheetsData(): ImportedData {
        val financialAssetRows = importRange(FINANCIAL_ASSETS_RANGE_NAME)
            .rows
            .map { SheetRowsParser.parseFinancialAsset(it) }
        val accountRows = importRange(ACCOUNTS_RANGE_NAME)
            .rows
            .map { SheetRowsParser.parseAccount(it) }
        val transactionRows = TRANSACTION_RANGE_NAMES
            .map { importRange(it) }
            .flatMap { it.rows }
            .map { SheetRowsParser.parseTransaction(it) }
        return ImportedData(financialAssetRows, accountRows, transactionRows)
    }

    private suspend fun importTransactionRanges(): List<SheetRange> =
        TRANSACTION_RANGE_NAMES.map { importRange(it) }

    private suspend fun importAccountsRange(): SheetRange =
        importRange(ACCOUNTS_RANGE_NAME)

    private suspend fun importFinancialAssetsRange(): SheetRange =
        importRange(FINANCIAL_ASSETS_RANGE_NAME)

    private suspend fun importRange(rangeName: String): SheetRange {
        log.info("Importing range $rangeName")
        val range = sheetsClient.getRange(googleConfig.accountsSpreadsheetId, rangeName)
        log.info("Imported range $rangeName")
        return range
    }

    private suspend fun convertAndInsertFinancialAssets(range: SheetRange): List<FinancialAsset> {
        val convertedAssets = range.rows.map { FinancialAssetConverter.convert(it) }
        val (optionAssets, filteredAssets) = convertedAssets.partition { it.type == FinancialAssetType.OPTION }
        val insertedAssets = financialAssetDao.insertFinancialAssets(filteredAssets)

        val enrichedOptionAssets = optionAssets.map { optionAsset ->
            val associatedAsset = insertedAssets
                .find { it.companyName == optionAsset.companyName }
                ?: throw IllegalArgumentException("Unexpected option asset company name ${optionAsset.companyName}")
            optionAsset.copy(optionAssetId = associatedAsset.id)
        }
        val insertedOptionAssets = financialAssetDao.insertFinancialAssets(enrichedOptionAssets)

        val allInsertedAssets = insertedAssets.plus(insertedOptionAssets)
        log.info("Inserted financial assets: $allInsertedAssets")
        return allInsertedAssets
    }

    private fun convertAccountsToImportDto(
        range: SheetRange,
        financialAssets: List<FinancialAsset>
    ): List<AccountImportDto> {
        val convertedAccounts = range.rows.map { AccountConverter.convertToImportDto(it, financialAssets) }
        log.info("Converted accounts: $convertedAccounts")
        return convertedAccounts
    }

    private fun convertTransactionsToImportDto(ranges: List<SheetRange>): List<TransactionImportDto> {
        val convertedTransactions = ranges
            .flatMap { it.rows }
            .map { TransactionConverter.convertToImportDto(it) }
        log.info("Converted transactions: $convertedTransactions")
        return convertedTransactions
    }

    private suspend fun insertAccounts(
        accountImportDtoList: List<AccountImportDto>,
        transactionImportDtoList: List<TransactionImportDto>
    ): List<Account> {
        val accounts = accountImportDtoList.map { AccountConverter.convert(it, transactionImportDtoList) }
        val insertedAccounts = accountDao.insertAccounts(accounts)
        log.info("Inserted accounts: $insertedAccounts")
        return insertedAccounts
    }

    private suspend fun insertTransactions(
        transactionImportDtoList: List<TransactionImportDto>,
        accounts: List<Account>
    ): List<Transaction> {
        val transactions = transactionImportDtoList.mapNotNull { TransactionConverter.convert(it, accounts) }
        val insertedTransactions = transactionDao.insertTransactions(transactions)
        log.info("Inserted transactions: $insertedTransactions")
        return insertedTransactions
    }

    companion object {
        private val TRANSACTION_RANGE_NAMES = (2015..2022).map { "$it!A2:F" }
        private const val ACCOUNTS_RANGE_NAME = "Счета!A2:B"
        private const val FINANCIAL_ASSETS_RANGE_NAME = "Счета!D22:K"
    }
}
