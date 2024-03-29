package com.ran.kolibri.common.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import com.github.salomonbrys.kodein.singleton
import com.google.api.services.sheets.v4.Sheets
import com.ran.kolibri.common.client.open.exchange.rates.OpenExchangeRatesClient
import com.ran.kolibri.common.client.open.exchange.rates.model.OpenExchangeRatesConfig
import com.ran.kolibri.common.client.sheets.SheetsClient
import com.ran.kolibri.common.client.sheets.buildSheets
import com.ran.kolibri.common.client.sheets.model.GoogleConfig
import com.ran.kolibri.common.client.telegram.TelegramClient
import com.ran.kolibri.common.client.telegram.model.TelegramConfig
import com.ran.kolibri.common.client.tinkoff.investing.TinkoffInvestingClient
import com.ran.kolibri.common.client.tinkoff.investing.model.TinkoffInvestingConfig
import com.ran.kolibri.common.config.DatabaseConfig
import com.ran.kolibri.common.config.Environment
import com.ran.kolibri.common.config.ServerConfig
import com.ran.kolibri.common.dao.AccountDao
import com.ran.kolibri.common.dao.CurrencyPriceDao
import com.ran.kolibri.common.dao.FinancialAssetDao
import com.ran.kolibri.common.dao.TelegramIntegrationDao
import com.ran.kolibri.common.dao.TelegramOperationDao
import com.ran.kolibri.common.dao.TransactionDao
import com.ran.kolibri.common.dao.impl.AccountDaoImpl
import com.ran.kolibri.common.dao.impl.CurrencyPriceDaoImpl
import com.ran.kolibri.common.dao.impl.FinancialAssetDaoImpl
import com.ran.kolibri.common.dao.impl.TelegramIntegrationDaoImpl
import com.ran.kolibri.common.dao.impl.TelegramOperationDaoImpl
import com.ran.kolibri.common.dao.impl.TransactionDaoImpl
import com.ran.kolibri.common.kafka.BytesKafkaProducer
import com.ran.kolibri.common.kafka.KafkaConfig
import com.ran.kolibri.common.kafka.producer.buildBytesKafkaProducer
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.common.util.buildDatabase
import com.ran.kolibri.common.util.buildHttpClient
import com.typesafe.config.Config
import io.ktor.client.HttpClient
import org.jetbrains.exposed.sql.Database

fun buildConfigModule(config: Config) = Kodein.Module {
    bind<Config>() with provider { config }
    bind<Environment>() with provider { Environment.build(config) }
    bind<ServerConfig>() with provider { ServerConfig(config) }
}

val httpClientModule = Kodein.Module {
    bind<HttpClient>() with provider { buildHttpClient() }
}

val kafkaModule = Kodein.Module {
    bind<KafkaConfig>() with provider { KafkaConfig(kodein.instance()) }
    bind<BytesKafkaProducer>() with singleton { buildBytesKafkaProducer(kodein) }
}

val daoModule = Kodein.Module {
    bind<DatabaseConfig>() with provider { DatabaseConfig(kodein.instance()) }
    bind<Database>() with singleton { buildDatabase(kodein) }
    bind<FinancialAssetDao>() with provider { FinancialAssetDaoImpl(kodein) }
    bind<AccountDao>() with provider { AccountDaoImpl(kodein) }
    bind<TransactionDao>() with provider { TransactionDaoImpl(kodein) }
    bind<CurrencyPriceDao>() with provider { CurrencyPriceDaoImpl(kodein) }
    bind<TelegramIntegrationDao>() with provider { TelegramIntegrationDaoImpl(kodein) }
    bind<TelegramOperationDao>() with provider { TelegramOperationDaoImpl(kodein) }
}

val telegramModule = Kodein.Module {
    bind<TelegramConfig>() with provider { TelegramConfig(kodein.instance()) }
    bind<TelegramClient>() with provider { TelegramClient(kodein) }
    bind<TelegramManager>() with provider { TelegramManager(kodein) }
}

val openExchangeRatesClientModule = Kodein.Module {
    bind<OpenExchangeRatesConfig>() with provider { OpenExchangeRatesConfig(kodein.instance()) }
    bind<OpenExchangeRatesClient>() with provider { OpenExchangeRatesClient(kodein) }
}

val tinkoffInvestingClientModule = Kodein.Module {
    bind<TinkoffInvestingConfig>() with provider { TinkoffInvestingConfig(kodein.instance()) }
    bind<TinkoffInvestingClient>() with provider { TinkoffInvestingClient(kodein) }
}

val sheetsModule = Kodein.Module {
    bind<GoogleConfig>() with provider { GoogleConfig(kodein.instance()) }
    bind<Sheets>() with singleton { buildSheets(kodein) }
    bind<SheetsClient>() with provider { SheetsClient(kodein) }
}
