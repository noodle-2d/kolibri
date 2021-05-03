package com.ran.kolibri.scheduler.manager.telegram

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.telegram.TelegramClient
import com.ran.kolibri.common.dao.TelegramIntegrationDao
import com.ran.kolibri.common.dao.TelegramOperationDao
import com.ran.kolibri.common.entity.TelegramOperation
import com.ran.kolibri.common.util.log
import com.ran.kolibri.scheduler.manager.importing.ImportOldSheetsManager
import com.ran.kolibri.scheduler.manager.statistics.AccountsStatisticsManager
import com.ran.kolibri.scheduler.manager.telegram.model.CallbackQuery
import com.ran.kolibri.scheduler.manager.telegram.model.OperationContinuation
import com.ran.kolibri.scheduler.manager.telegram.model.OperationInitiation
import com.ran.kolibri.scheduler.manager.telegram.model.OperationUpdate
import com.ran.kolibri.scheduler.manager.telegram.model.PlainText
import com.ran.kolibri.scheduler.manager.telegram.model.TelegramUpdate
import com.ran.kolibri.scheduler.manager.transaction.AddTransactionManager
import org.joda.time.DateTime

class TelegramUpdatesManager(kodein: Kodein) {

    private val telegramClient: TelegramClient = kodein.instance()
    private val telegramUpdateRecognizer: TelegramUpdateRecognizer = kodein.instance()
    private val telegramIntegrationDao: TelegramIntegrationDao = kodein.instance()
    private val telegramOperationDao: TelegramOperationDao = kodein.instance()

    private val importOldSheetsManager: ImportOldSheetsManager = kodein.instance()
    private val accountsStatisticsManager: AccountsStatisticsManager = kodein.instance()
    private val addTransactionManager: AddTransactionManager = kodein.instance()

    private val telegramUpdateProcessors = listOf(
        importOldSheetsManager,
        accountsStatisticsManager,
        addTransactionManager
    )

    suspend fun pullUpdates() {
        val telegramIntegration = telegramIntegrationDao.get()
        val updatesResponse = telegramClient.getUpdates(telegramIntegration.lastUpdateId + 1)

        val updates = updatesResponse.result.orEmpty()
        updates.forEach {
            val telegramUpdate = telegramUpdateRecognizer.recognize(it)
            log.info("Processing update $it, recognized as $telegramUpdate")
            processUpdate(telegramUpdate)
        }

        updates.lastOrNull()?.updateId?.let { newLastUpdateId ->
            val updatedTelegramIntegration = telegramIntegration.copy(lastUpdateId = newLastUpdateId)
            telegramIntegrationDao.update(updatedTelegramIntegration)
            log.info("Processed ${updates.size} updates, last update id = $newLastUpdateId")
        }
    }

    private suspend fun processUpdate(update: TelegramUpdate) =
        when (update) {
            is OperationInitiation -> processOperationInitiation(update)
            is OperationContinuation -> processOperationContinuation(update)
            else -> ignoreUnknownUpdate(update)
        }

    private suspend fun processOperationInitiation(update: OperationInitiation) {
        val telegramOperation = TelegramOperation(
            operationName = update.type.operationName,
            messageId = null,
            context = null,
            createTime = DateTime.now()
        )
        val insertedTelegramOperation = telegramOperationDao.insert(telegramOperation)
        processOperation(insertedTelegramOperation, update)
    }

    private suspend fun processOperationContinuation(update: OperationContinuation) {
        val foundTelegramOperation = when (update) {
            is CallbackQuery -> telegramOperationDao.findByMessageId(update.messageId)
            is PlainText -> telegramOperationDao.findLast()
            else -> null
        }

        foundTelegramOperation
            ?.takeIf { isOperationActive(it) }
            ?.let { processOperation(it, update) }
            ?: ignoreUnknownUpdate(update)
    }

    private suspend fun processOperation(operation: TelegramOperation, update: OperationUpdate) {
        val chosenProcessor = telegramUpdateProcessors
            .find { it.operationType.operationName == operation.operationName }
            ?: IgnoreUpdateProcessor
        log.info("Processor ${chosenProcessor.javaClass.simpleName} was chosen for update $update")
        val updatedOperation = chosenProcessor.processUpdate(operation, update)
        telegramOperationDao.update(updatedOperation)
    }

    private fun isOperationActive(operation: TelegramOperation): Boolean =
        operation.createTime.plusHours(1).isAfterNow

    private fun ignoreUnknownUpdate(update: TelegramUpdate) =
        log.info("Ignoring processing unknown update $update")
}
