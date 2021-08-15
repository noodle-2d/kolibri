package com.ran.kolibri.consumer.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.common.kafka.COMPLICATED_TEST_EVENT_ID
import com.ran.kolibri.common.kafka.TEST_EVENT_ID
import com.ran.kolibri.common.kafka.message.ComplicatedTestEvent
import com.ran.kolibri.common.kafka.message.TestEvent
import com.ran.kolibri.common.kafka.processor.MessageProcessor
import com.ran.kolibri.common.kafka.processor.MessageProcessorLauncher
import com.ran.kolibri.common.kafka.processor.buildJsonMessageProcessorLauncher
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.consumer.processor.ComplicatedTestEventProcessor
import com.ran.kolibri.consumer.processor.TestEventProcessor

val managerModule = Kodein.Module {
    bind<TelegramManager>() with provider { TelegramManager(kodein) }
}

val processorModule = Kodein.Module {
    bind<MessageProcessor<TestEvent>>() with provider { TestEventProcessor(kodein) }
    bind<MessageProcessor<ComplicatedTestEvent>>() with provider { ComplicatedTestEventProcessor(kodein) }
}

val processorLauncherModule = Kodein.Module {
    bind<MessageProcessorLauncher>(TEST_EVENT_ID) with provider {
        buildJsonMessageProcessorLauncher(kodein, TEST_EVENT_ID, TestEvent::class.java, kodein.instance())
    }
    bind<MessageProcessorLauncher>(COMPLICATED_TEST_EVENT_ID) with provider {
        buildJsonMessageProcessorLauncher(
            kodein,
            COMPLICATED_TEST_EVENT_ID,
            ComplicatedTestEvent::class.java,
            kodein.instance()
        )
    }
}
