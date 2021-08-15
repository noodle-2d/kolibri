package com.ran.kolibri.common.starter

import com.github.salomonbrys.kodein.Kodein
import com.ran.kolibri.common.kafka.processor.MessageProcessorLauncher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

interface ConsumerStarter {

    fun getMessageProcessorLaunchers(kodein: Kodein): List<MessageProcessorLauncher>

    suspend fun startConsumers(kodein: Kodein) = coroutineScope {
        getMessageProcessorLaunchers(kodein).forEach { launcher ->
            async {
                launcher.launchMessageProcessing()
            }
        }
    }
}
