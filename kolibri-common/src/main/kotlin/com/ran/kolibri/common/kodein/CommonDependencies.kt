package com.ran.kolibri.common.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.google.api.services.sheets.v4.Sheets
import com.ran.kolibri.common.client.SheetsClient
import com.ran.kolibri.common.client.TelegramBotClient
import com.ran.kolibri.common.client.telegram.TelegramClient
import com.ran.kolibri.common.client.telegram.model.TelegramConfig
import com.ran.kolibri.common.dao.AccountDao
import com.ran.kolibri.common.dao.FinancialAssetDao
import com.ran.kolibri.common.dao.TelegramIntegrationDao
import com.ran.kolibri.common.dao.TransactionDao
import com.ran.kolibri.common.dao.impl.AccountDaoImpl
import com.ran.kolibri.common.dao.impl.FinancialAssetDaoImpl
import com.ran.kolibri.common.dao.impl.TelegramIntegrationDaoImpl
import com.ran.kolibri.common.dao.impl.TransactionDaoImpl
import com.ran.kolibri.common.dto.config.DatabaseConfig
import com.ran.kolibri.common.dto.config.Environment
import com.ran.kolibri.common.dto.config.GoogleConfig
import com.ran.kolibri.common.dto.config.ServerConfig
import com.ran.kolibri.common.dto.config.TelegramBotClientConfig
import com.ran.kolibri.common.util.buildDatabase
import com.ran.kolibri.common.util.buildHttpClient
import com.ran.kolibri.common.util.buildSheets
import com.typesafe.config.Config
import io.ktor.client.HttpClient
import org.jetbrains.exposed.sql.Database

fun buildConfigModule(config: Config) = Kodein.Module {
    bind<Config>() with provider { config }
    bind<Environment>() with provider { Environment.build(config) }
    bind<ServerConfig>() with provider { ServerConfig(config) }
    bind<DatabaseConfig>() with provider { DatabaseConfig(config) }
}

val httpClientModule = Kodein.Module {
    bind<HttpClient>() with provider { buildHttpClient() }
}

val daoModule = Kodein.Module {
    bind<Database>() with provider { buildDatabase(kodein) }
    bind<FinancialAssetDao>() with provider { FinancialAssetDaoImpl(kodein) }
    bind<AccountDao>() with provider { AccountDaoImpl(kodein) }
    bind<TransactionDao>() with provider { TransactionDaoImpl(kodein) }
    bind<TelegramIntegrationDao>() with provider { TelegramIntegrationDaoImpl(kodein) }
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

val telegramBotClientModule = Kodein.Module {
    bind<TelegramBotClientConfig>() with provider { TelegramBotClientConfig(kodein.instance<Config>()) }
    bind<TelegramBotClient>() with provider { TelegramBotClient(kodein) }
}
