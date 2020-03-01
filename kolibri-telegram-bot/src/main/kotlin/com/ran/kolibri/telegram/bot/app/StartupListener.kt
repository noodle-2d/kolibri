package com.ran.kolibri.telegram.bot.app

import com.ran.kolibri.common.util.logInfo
import com.ran.kolibri.telegram.bot.service.TelegramService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class StartupListener {

    @Autowired
    private lateinit var telegramService: TelegramService

    @EventListener
    fun onEvent(event: ContextRefreshedEvent) {
        logInfo { "Processing application startup event" }
        telegramService.setWebhook()
        logInfo { "Application startup event was successfully processed" }
    }
}
