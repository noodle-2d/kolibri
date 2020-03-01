package com.ran.kolibri.telegram.bot.app

import com.ran.kolibri.common.telegram.TelegramConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@SpringBootApplication
@ComponentScan(basePackages = ["com.ran.kolibri"])
@Import(TelegramConfig::class)
class AppConfig
