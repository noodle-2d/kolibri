package com.ran.kolibri.commandline.utility.listener

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.commandline.utility.dto.action.Action
import com.ran.kolibri.commandline.utility.dto.action.ImportOldSheets
import com.ran.kolibri.commandline.utility.dto.action.Unknown
import com.ran.kolibri.commandline.utility.service.ImportOldSheetsService
import com.ran.kolibri.common.client.TelegramBotClient
import com.ran.kolibri.common.dto.telegram.bot.utils.SendMessageToOwnerBotRequest
import com.ran.kolibri.common.listener.StartupListener
import com.ran.kolibri.common.util.log
import java.lang.IllegalArgumentException

class ActionListener(private val kodein: Kodein) : StartupListener {

    private val action: Action = kodein.instance()
    private val telegramBotClient: TelegramBotClient = kodein.instance()

    override suspend fun processStartup() {
        val processor = when (action) {
            is ImportOldSheets -> kodein.instance<ImportOldSheetsService>()
            is Unknown -> throw IllegalArgumentException("Unknown action ${action.name}")
        }

        val resultMessage = try {
            val result = processor.processAction()
            result.asString()
        } catch (e: Throwable) {
            val errorMessage = "Error while processing action ${action.javaClass.simpleName}}"
            log.error(errorMessage, e)
            "$errorMessage: ${e.message ?: "empty message"}."
        }

        val fullMessage = "Processed action ${action.javaClass.simpleName}.\n$resultMessage"
        val telegramBotRequest = SendMessageToOwnerBotRequest(fullMessage)
        telegramBotClient.sendMessageToOwner(telegramBotRequest)
    }
}
