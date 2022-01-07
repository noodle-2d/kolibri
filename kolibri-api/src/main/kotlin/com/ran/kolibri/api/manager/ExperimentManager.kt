package com.ran.kolibri.api.manager

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.kafka.message.ComplicatedTestEvent
import com.ran.kolibri.common.kafka.message.TestEvent
import com.ran.kolibri.common.kafka.producer.MessageProducer
import com.ran.kolibri.common.util.log
import org.joda.time.DateTime
import java.security.SecureRandom

class ExperimentManager(kodein: Kodein) {

    private val random = SecureRandom()
    private val testEventsProducer: MessageProducer<TestEvent> = kodein.instance()
    private val complicatedTestEventsProducer: MessageProducer<ComplicatedTestEvent> = kodein.instance()

    suspend fun sendTestEvent() =
        if (random.nextBoolean()) {
            val event = generateTestEvent()
            log.info("Generated event $event")
            testEventsProducer.produce(event)
            log.info("Produced event $event")
        } else {
            val event = generateComplicatedTestEvent()
            log.info("Generated event $event")
            complicatedTestEventsProducer.produce(event)
            log.info("Produced event $event")
        }

    private fun generateComplicatedTestEvent(): ComplicatedTestEvent {
        val eventsQuantity = random.nextInt(4) + 1
        val testEvents = (0..eventsQuantity).map { generateTestEvent() }
        return ComplicatedTestEvent(generateString(), testEvents)
    }

    private fun generateTestEvent(): TestEvent =
        TestEvent(
            generateString(),
            random.nextInt().toLong(),
            DateTime.now()
        )

    private fun generateString(): String {
        val length = random.nextInt(11) + 5
        val charList = (0..length).map { 'a' + random.nextInt(25) }
        return String(charList.toCharArray())
    }
}
