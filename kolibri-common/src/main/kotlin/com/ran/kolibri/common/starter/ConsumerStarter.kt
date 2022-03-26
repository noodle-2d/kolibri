package com.ran.kolibri.common.starter

import com.github.salomonbrys.kodein.Kodein
import com.ran.kolibri.common.kafka.processor.MessageProcessorLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

interface ConsumerStarter {

    fun getMessageProcessorLaunchers(kodein: Kodein): List<MessageProcessorLauncher>

    fun CoroutineScope.startConsumers(kodein: Kodein) =
        getMessageProcessorLaunchers(kodein).forEach { launcher ->
            async {
                launcher.launchMessageProcessing()
            }
        }
}
