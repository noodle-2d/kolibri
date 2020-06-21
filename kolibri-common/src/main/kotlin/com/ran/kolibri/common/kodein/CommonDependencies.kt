package com.ran.kolibri.common.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.google.api.services.sheets.v4.Sheets
import com.ran.kolibri.common.client.SheetsClient
import com.ran.kolibri.common.client.TelegramClient
import com.ran.kolibri.common.dto.config.Environment
import com.ran.kolibri.common.dto.config.GoogleConfig
import com.ran.kolibri.common.dto.config.ServerConfig
import com.ran.kolibri.common.dto.config.TelegramConfig
import com.ran.kolibri.common.util.buildHttpClient
import com.ran.kolibri.common.util.buildSheets
import com.typesafe.config.Config
import io.ktor.client.HttpClient

fun buildConfigModule(config: Config) = Kodein.Module {
    bind<Config>() with provider { config }
    bind<Environment>() with provider { Environment.build(config) }
    bind<ServerConfig>() with provider { ServerConfig(config) }
}

val httpClientModule = Kodein.Module {
    bind<HttpClient>() with provider { buildHttpClient() }
}

val telegramClientModule = Kodein.Module {
    bind<TelegramConfig>() with provider { TelegramConfig(kodein.instance()) }
    bind<TelegramClient>() with provider { TelegramClient(kodein) }
}

val sheetsModule = Kodein.Module {
    bind<GoogleConfig>() with provider { GoogleConfig(kodein.instance()) }
    bind<Sheets>() with provider { buildSheets(kodein) }
    bind<SheetsClient>() with provider { SheetsClient(kodein) }
}
