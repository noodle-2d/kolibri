package com.ran.kolibri.consumer.starter

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.kafka.COMPLICATED_TEST_EVENT_ID
import com.ran.kolibri.common.kafka.TEST_EVENT_ID
import com.ran.kolibri.common.kafka.processor.MessageProcessorLauncher
import com.ran.kolibri.common.kodein.httpClientModule
import com.ran.kolibri.common.kodein.kafkaModule
import com.ran.kolibri.common.kodein.telegramClientModule
import com.ran.kolibri.common.starter.BaseStarter
import com.ran.kolibri.common.starter.ConsumerStarter
import com.ran.kolibri.consumer.kodein.managerModule
import com.ran.kolibri.consumer.kodein.processorLauncherModule
import com.ran.kolibri.consumer.kodein.processorModule

class KolibriConsumerStarter : BaseStarter(), ConsumerStarter {

    override fun getKodeinModules(): List<Kodein.Module> =
        listOf(
            httpClientModule,
            telegramClientModule,
            kafkaModule,
            managerModule,
            processorModule,
            processorLauncherModule
        )

    override fun getMessageProcessorLaunchers(kodein: Kodein): List<MessageProcessorLauncher> =
        listOf(
            kodein.instance(TEST_EVENT_ID),
            kodein.instance(COMPLICATED_TEST_EVENT_ID)
        )
}
