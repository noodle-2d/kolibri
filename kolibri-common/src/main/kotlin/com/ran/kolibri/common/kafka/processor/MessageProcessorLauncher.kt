package com.ran.kolibri.common.kafka.processor

interface MessageProcessorLauncher {
    suspend fun launchMessageProcessing()
}
