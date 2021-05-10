package com.ran.kolibri.common.starter

import com.github.salomonbrys.kodein.Kodein
import com.ran.kolibri.common.util.log
import kotlinx.coroutines.coroutineScope

interface ConsumerStarter {

    suspend fun startConsumers(kodein: Kodein) = coroutineScope {
        log.info("Started consumer") // todo
    }
}
