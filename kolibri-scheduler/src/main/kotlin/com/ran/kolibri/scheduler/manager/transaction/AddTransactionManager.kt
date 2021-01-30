package com.ran.kolibri.scheduler.manager.transaction

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.telegram.model.Update
import com.ran.kolibri.common.manager.ChatContext
import com.ran.kolibri.common.manager.Ignored
import com.ran.kolibri.common.manager.ListOption
import com.ran.kolibri.common.manager.Processed
import com.ran.kolibri.common.manager.ProcessingInContextResult
import com.ran.kolibri.common.manager.TelegramManager

class AddTransactionManager(kodein: Kodein) {

    private val telegramManager: TelegramManager = kodein.instance()

    suspend fun startAddingTransaction() {
        telegramManager.doActionSettingChatContext {
            val options = listOf(ListOption("First option", "1"), ListOption("Second option", "2"))
            val messageId = telegramManager.sendMessageToOwner("Start adding transaction", options)
            AddTransactionContext(messageId!!)
        }
    }

    suspend fun processAddingTransactionInContext(
        context: AddTransactionContext,
        update: Update
    ): ProcessingInContextResult =
        if (update.callbackQuery?.message?.messageId != context.messageId) {
            Ignored
        } else {
            telegramManager.sendMessageToOwner("Chosen option is ${update.callbackQuery?.data}")
            Processed(null)
        }
}

data class AddTransactionContext(override val messageId: Int) : ChatContext
