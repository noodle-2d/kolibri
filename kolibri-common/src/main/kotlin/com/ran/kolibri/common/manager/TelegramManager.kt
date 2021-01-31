package com.ran.kolibri.common.manager

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.telegram.TelegramClient
import com.ran.kolibri.common.client.telegram.model.Button
import com.ran.kolibri.common.client.telegram.model.EditButtonMessageRequest
import com.ran.kolibri.common.client.telegram.model.EditTextMessageRequest
import com.ran.kolibri.common.client.telegram.model.ReplyMarkup
import com.ran.kolibri.common.client.telegram.model.SendButtonMessageRequest
import com.ran.kolibri.common.client.telegram.model.SendTextMessageRequest
import com.ran.kolibri.common.client.telegram.model.TelegramConfig
import com.ran.kolibri.common.util.log
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import org.joda.time.DateTime

class TelegramManager(kodein: Kodein) {

    private val telegramClient: TelegramClient = kodein.instance()
    private val telegramConfig: TelegramConfig = kodein.instance()

    suspend fun sendMessageToOwner(text: String, options: List<ListOption> = listOf()): Int {
        log.info("Sending message to owner: $text [$options]")

        val sendMessageRequest = when (val replyMarkup = buildReplyMarkup(options)) {
            null -> SendTextMessageRequest(telegramConfig.botOwnerId, text)
            else -> SendButtonMessageRequest(telegramConfig.botOwnerId, text, replyMarkup)
        }
        val sendMessageResponse = telegramClient.sendMessage(sendMessageRequest)

        log.info("Message was successfully sent. Response: $sendMessageResponse")
        return sendMessageResponse.result!!.messageId
    }

    suspend fun editMessageToOwner(messageId: Int, text: String, options: List<ListOption> = listOf()): Int {
        log.info("Edit message $messageId to owner: $text [$options]")

        val editMessageRequest = when (val replyMarkup = buildReplyMarkup(options)) {
            null -> EditTextMessageRequest(telegramConfig.botOwnerId, messageId, text)
            else -> EditButtonMessageRequest(telegramConfig.botOwnerId, messageId, text, replyMarkup)
        }
        val editMessageResponse = telegramClient.editMessage(editMessageRequest)

        log.info("Message was successfully edited. Response: $editMessageResponse")
        return editMessageResponse.result!!.messageId
    }

    private fun buildReplyMarkup(options: List<ListOption>): ReplyMarkup? =
        options
            .map { listOf(Button(it.name, it.id)) }
            .let { if (it.isEmpty()) null else it }
            ?.let { ReplyMarkup(it) }

    suspend fun doActionSettingChatContext(action: suspend () -> ChatContext) =
        doActionUpdatingChatContext { Processed(action()) }

    suspend fun doActionUpdatingChatContext(action: suspend (ChatContext?) -> ProcessingInContextResult) =
        withContext(threadContext) {
            if (lastChatContextUpdateTime.plusHours(1).isBeforeNow) {
                updateContext(null)
            }

            when (val processingResult = action(chatContext)) {
                is Processed -> updateContext(processingResult.newChatContext)
            }
        }

    private fun updateContext(newContext: ChatContext?) {
        chatContext = newContext
        lastChatContextUpdateTime = DateTime.now()
    }

    companion object {
        private val threadContext = newSingleThreadContext("ChatContext")
        private var lastChatContextUpdateTime = DateTime.now()
        private var chatContext: ChatContext? = null
    }
}

interface ChatContext {
    val messageId: Int
}

interface ProcessingInContextResult

object Ignored : ProcessingInContextResult

data class Processed(val newChatContext: ChatContext?) : ProcessingInContextResult

data class ListOption(val name: String, val id: String)
