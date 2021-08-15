package com.ran.kolibri.consumer.processor

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.kafka.message.TestEvent
import com.ran.kolibri.common.kafka.processor.MessageProcessor
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.common.util.log

class TestEventProcessor(kodein: Kodein) : MessageProcessor<TestEvent> {

    private val telegramManager: TelegramManager = kodein.instance()

    override suspend fun process(message: TestEvent) {
        val textMessage = "Got test event: $message"
        telegramManager.sendMessageToOwner(textMessage)
        log.info("Processed test event: $message")
    }
}
