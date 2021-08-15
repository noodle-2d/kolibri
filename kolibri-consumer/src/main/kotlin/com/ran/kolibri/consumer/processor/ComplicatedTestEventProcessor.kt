package com.ran.kolibri.consumer.processor

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.kafka.message.ComplicatedTestEvent
import com.ran.kolibri.common.kafka.processor.MessageProcessor
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.common.util.log

class ComplicatedTestEventProcessor(kodein: Kodein) : MessageProcessor<ComplicatedTestEvent> {

    private val telegramManager: TelegramManager = kodein.instance()

    override suspend fun process(message: ComplicatedTestEvent) {
        val innerTextMessages = message.events
            .orEmpty()
            .map { "\n    $it" }
            .let { if (it.isNotEmpty()) it.reduce { acc, s -> "$acc$s" } else " empty" }
        val textMessage = "Got complicated test event ${message.id}:$innerTextMessages"
        telegramManager.sendMessageToOwner(textMessage)
        log.info("Processed complicated test event: $message")
    }
}
