package com.ran.kolibri.common.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.common.dto.config.ServerConfig
import com.ran.kolibri.common.dto.config.TelegramClientConfig
import com.ran.kolibri.common.http.buildHttpClient
import com.ran.kolibri.common.telegram.TelegramClient
import com.typesafe.config.Config
import io.ktor.client.HttpClient

fun buildConfigModule(config: Config) = Kodein.Module {
    bind<Config>() with provider { config }
    bind<ServerConfig>() with provider { ServerConfig(config) }
    bind<TelegramClientConfig>() with provider { TelegramClientConfig(config) }
}

val httpClientModule = Kodein.Module {
    bind<HttpClient>() with provider { buildHttpClient() }
}

val telegramClientModule = Kodein.Module {
    bind<TelegramClient>() with provider { TelegramClient(kodein) }
}
