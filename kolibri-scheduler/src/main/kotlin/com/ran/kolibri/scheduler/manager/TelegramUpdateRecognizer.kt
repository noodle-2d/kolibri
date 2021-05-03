package com.ran.kolibri.scheduler.manager

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.telegram.model.TelegramConfig
import com.ran.kolibri.common.client.telegram.model.Update
import com.ran.kolibri.scheduler.manager.model.telegram.CallbackQuery
import com.ran.kolibri.scheduler.manager.model.telegram.OperationInitiation
import com.ran.kolibri.scheduler.manager.model.telegram.PlainText
import com.ran.kolibri.scheduler.manager.model.telegram.TelegramOperationType
import com.ran.kolibri.scheduler.manager.model.telegram.TelegramUpdate
import com.ran.kolibri.scheduler.manager.model.telegram.UnknownChatUpdate

class TelegramUpdateRecognizer(kodein: Kodein) {

    private val telegramConfig: TelegramConfig = kodein.instance()

    fun recognize(update: Update): TelegramUpdate =
        when {
            getChatId(update) != telegramConfig.botOwnerId -> UnknownChatUpdate(getChatId(update))
            getOperationInitiation(update) != null -> getOperationInitiation(update)!!
            getCallbackMessageId(update) != null ->
                CallbackQuery(getCallbackMessageId(update)!!, getCallbackData(update))
            else -> PlainText(getMessageText(update))
        }

    private fun getChatId(update: Update): Int? =
        update.message?.chat?.id ?: update.callbackQuery?.message?.chat?.id

    private fun getMessageText(update: Update): String =
        update.message?.text.orEmpty()

    private fun getCallbackMessageId(update: Update): Int? =
        update.callbackQuery?.message?.messageId

    private fun getCallbackData(update: Update): String =
        update.callbackQuery?.data!!

    private fun getOperationInitiation(update: Update): OperationInitiation? =
        TelegramOperationType
            .values()
            .find { it.operationName == getMessageText(update) }
            ?.let { OperationInitiation(it) }
}
