package com.ran.kolibri.telegram.bot.listener

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.config.Environment
import com.ran.kolibri.common.listener.StartupListener
import com.ran.kolibri.telegram.bot.service.TelegramService

class SetWebhookListener(kodein: Kodein) : StartupListener {

    private val telegramService: TelegramService = kodein.instance()
    private val environment: Environment = kodein.instance()

    override suspend fun processStartup() =
        when (environment) {
            Environment.PRODUCTION -> telegramService.setWebhook()
            Environment.DEVELOPMENT -> Unit
        }
}
