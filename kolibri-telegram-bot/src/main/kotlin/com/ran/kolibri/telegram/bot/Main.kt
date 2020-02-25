package com.ran.kolibri.telegram.bot

import com.ran.kolibri.telegram.bot.app.AppConfig
import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    SpringApplication.run(AppConfig::class.java, *args)
}
