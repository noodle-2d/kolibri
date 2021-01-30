package com.ran.kolibri.scheduler.manager

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.telegram.TelegramClient
import com.ran.kolibri.common.client.telegram.model.TelegramConfig
import com.ran.kolibri.common.client.telegram.model.Update
import com.ran.kolibri.common.dao.TelegramIntegrationDao
import com.ran.kolibri.common.manager.Ignored
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.common.util.log
import com.ran.kolibri.scheduler.manager.importing.ImportOldSheetsManager
import com.ran.kolibri.scheduler.manager.statistics.AccountsStatisticsManager
import com.ran.kolibri.scheduler.manager.transaction.AddTransactionContext
import com.ran.kolibri.scheduler.manager.transaction.AddTransactionManager

class TelegramUpdatesManager(kodein: Kodein) {

    private val telegramClient: TelegramClient = kodein.instance()
    private val telegramConfig: TelegramConfig = kodein.instance()
    private val telegramManager: TelegramManager = kodein.instance()
    private val telegramIntegrationDao: TelegramIntegrationDao = kodein.instance()

    private val importOldSheetsManager: ImportOldSheetsManager = kodein.instance()
    private val accountsStatisticsManager: AccountsStatisticsManager = kodein.instance()
    private val addTransactionManager: AddTransactionManager = kodein.instance()

    suspend fun pullUpdates() {
        val telegramIntegration = telegramIntegrationDao.get()
        val updatesResponse = telegramClient.getUpdates(telegramIntegration.lastUpdateId + 1)

        val updates = updatesResponse.result.orEmpty()
        updates.forEach { processUpdate(it) }

        updates.lastOrNull()?.updateId?.let { newLastUpdateId ->
            val updatedTelegramIntegration = telegramIntegration.copy(lastUpdateId = newLastUpdateId)
            telegramIntegrationDao.update(updatedTelegramIntegration)
            log.info("Processed ${updates.size} updates, last update id = $newLastUpdateId")
        }
    }

    private suspend fun processUpdate(update: Update) {
        log.info("Processing update $update")

        val chatId = update.message?.chat?.id ?: update.callbackQuery?.message?.chat?.id
        if (chatId != telegramConfig.botOwnerId) {
            log.info("Ignoring message from chat $chatId")
            return
        }

        if (processOperationStart(update)) {
            return
        }

        processInContext(update)
    }

    private suspend fun processOperationStart(update: Update): Boolean {
        when (update.message?.text.orEmpty()) {
            "/import_old_sheets" -> importOldSheetsManager.importOldSheets()
            "/export_sheets" -> telegramManager.sendMessageToOwner("Exporting sheets is not supported yet")
            "/show_accounts_stat" -> accountsStatisticsManager.buildAccountsStatistics()
            "/show_total_stat" -> telegramManager.sendMessageToOwner("Showing statistics is not supported yet")
            "/add_transaction" -> addTransactionManager.startAddingTransaction()
            else -> return false
        }
        return true
    }

    private suspend fun processInContext(update: Update) =
        telegramManager.doActionUpdatingChatContext { chatContext ->
            when (chatContext) {
                is AddTransactionContext -> addTransactionManager.processAddingTransactionInContext(chatContext, update)
                else -> {
                    log.info("Ignoring unexpected update $update")
                    Ignored
                }
            }
        }
}
