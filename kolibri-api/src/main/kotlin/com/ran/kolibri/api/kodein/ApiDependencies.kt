package com.ran.kolibri.api.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.api.manager.AccountManager
import com.ran.kolibri.api.manager.ExperimentManager
import com.ran.kolibri.api.rest.AccountController
import com.ran.kolibri.api.rest.ExperimentController
import com.ran.kolibri.api.rest.TelegramController
import com.ran.kolibri.common.kafka.COMPLICATED_TEST_EVENT_ID
import com.ran.kolibri.common.kafka.TEST_EVENT_ID
import com.ran.kolibri.common.kafka.message.ComplicatedTestEvent
import com.ran.kolibri.common.kafka.message.TestEvent
import com.ran.kolibri.common.kafka.producer.MessageProducer
import com.ran.kolibri.common.kafka.producer.buildJsonMessageProducer
import com.ran.kolibri.common.manager.TelegramManager

val managerModule = Kodein.Module {
    bind<TelegramManager>() with provider { TelegramManager(kodein) }
    bind<AccountManager>() with provider { AccountManager(kodein) }
    bind<ExperimentManager>() with provider { ExperimentManager(kodein) }
}

val restModule = Kodein.Module {
    bind<TelegramController>() with provider { TelegramController(kodein) }
    bind<AccountController>() with provider { AccountController(kodein) }
    bind<ExperimentController>() with provider { ExperimentController(kodein) }
}

val kafkaModule = Kodein.Module {
    bind<MessageProducer<TestEvent>>() with provider {
        buildJsonMessageProducer(kodein, TEST_EVENT_ID, TestEvent::class.java)
    }
    bind<MessageProducer<ComplicatedTestEvent>>() with provider {
        buildJsonMessageProducer(kodein, COMPLICATED_TEST_EVENT_ID, ComplicatedTestEvent::class.java)
    }
}
