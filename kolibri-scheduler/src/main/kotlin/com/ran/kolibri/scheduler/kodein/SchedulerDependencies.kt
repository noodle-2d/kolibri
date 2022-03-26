package com.ran.kolibri.scheduler.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.scheduler.manager.importing.ImportSheetsManager
import com.ran.kolibri.scheduler.manager.importing.TransactionEnrichManager
import com.ran.kolibri.scheduler.manager.prices.CurrencyPricesManager
import com.ran.kolibri.scheduler.manager.statistics.AccountsStatisticsManager
import com.ran.kolibri.scheduler.manager.telegram.TelegramUpdateRecognizer
import com.ran.kolibri.scheduler.manager.telegram.TelegramUpdatesManager
import com.ran.kolibri.scheduler.manager.transaction.AddFinancialAssetTransactionManager
import com.ran.kolibri.scheduler.manager.transaction.AddTransactionManager
import com.ran.kolibri.scheduler.scheduled.task.CurrencyPricesUpdateTask
import com.ran.kolibri.scheduler.scheduled.task.SheetsImportTask
import com.ran.kolibri.scheduler.scheduled.task.TelegramPullTask

val managerModule = Kodein.Module {
    bind<TelegramUpdateRecognizer>() with provider { TelegramUpdateRecognizer(kodein) }
    bind<TelegramUpdatesManager>() with provider { TelegramUpdatesManager(kodein) }

    bind<ImportSheetsManager>() with provider { ImportSheetsManager(kodein) }
    bind<TransactionEnrichManager>() with provider { TransactionEnrichManager(kodein) }

    bind<AccountsStatisticsManager>() with provider { AccountsStatisticsManager(kodein) }

    bind<CurrencyPricesManager>() with provider { CurrencyPricesManager(kodein) }

    bind<AddTransactionManager>() with provider { AddTransactionManager(kodein) }
    bind<AddFinancialAssetTransactionManager>() with provider { AddFinancialAssetTransactionManager(kodein) }
}

val scheduledTaskModule = Kodein.Module {
    bind<SheetsImportTask>() with provider { SheetsImportTask(kodein) }
    bind<TelegramPullTask>() with provider { TelegramPullTask(kodein) }
    bind<CurrencyPricesUpdateTask>() with provider { CurrencyPricesUpdateTask(kodein) }
}
