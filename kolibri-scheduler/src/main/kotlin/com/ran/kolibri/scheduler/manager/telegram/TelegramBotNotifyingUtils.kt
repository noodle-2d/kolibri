package com.ran.kolibri.scheduler.manager.telegram

import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.common.util.log

interface TelegramBotNotifyingUtils {

    val telegramManager: TelegramManager

    suspend fun doActionSendingMessageToOwner(actionName: String, action: suspend () -> String) {
        telegramManager.sendMessageToOwner("Started $actionName")

        val resultMessage = try {
            action()
        } catch (e: Throwable) {
            val errorMessage = "Error while $actionName"
            log.error(errorMessage, e)
            "$errorMessage: ${e.message ?: "empty message"}."
        }

        telegramManager.sendMessageToOwner(resultMessage)
    }
}
