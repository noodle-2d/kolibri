package com.ran.kolibri.scheduler.manager.transaction

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.manager.Processed
import com.ran.kolibri.common.manager.ProcessingInContextResult
import com.ran.kolibri.common.manager.TelegramManager

class AddFinancialAssetTransactionManager(kodein: Kodein) {

    private val telegramManager: TelegramManager = kodein.instance()

    suspend fun startAddingFinancialAssetTransaction(messageId: Int): ProcessingInContextResult {
        telegramManager.editMessageToOwner(messageId, "Adding financial asset transaction. (todo)")
        return Processed(null)
    }
}

data class AddFinancialAssetTransactionContext(
    override val messageId: Int
) : AddTransactionContext
