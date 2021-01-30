package com.ran.kolibri.scheduler.kodein

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.scheduler.manager.TelegramUpdatesManager
import com.ran.kolibri.scheduler.manager.importing.ImportOldSheetsManager
import com.ran.kolibri.scheduler.manager.importing.TransactionEnrichManager
import com.ran.kolibri.scheduler.manager.prices.CurrencyPricesManager
import com.ran.kolibri.scheduler.manager.statistics.AccountsStatisticsManager
import com.ran.kolibri.scheduler.manager.transaction.AddTransactionManager
import com.ran.kolibri.scheduler.scheduled.task.CurrencyPricesUpdateTask
import com.ran.kolibri.scheduler.scheduled.task.OldSheetsImportTask
import com.ran.kolibri.scheduler.scheduled.task.SheetsExportTask
import com.ran.kolibri.scheduler.scheduled.task.TelegramPullTask

val managerModule = Kodein.Module {
    bind<TelegramManager>() with provider { TelegramManager(kodein) }
    bind<TelegramUpdatesManager>() with provider { TelegramUpdatesManager(kodein) }

    bind<ImportOldSheetsManager>() with provider { ImportOldSheetsManager(kodein) }
    bind<TransactionEnrichManager>() with provider { TransactionEnrichManager(kodein) }

    bind<AccountsStatisticsManager>() with provider { AccountsStatisticsManager(kodein) }

    bind<CurrencyPricesManager>() with provider { CurrencyPricesManager(kodein) }

    bind<AddTransactionManager>() with provider { AddTransactionManager(kodein) }
}

val scheduledTaskModule = Kodein.Module {
    bind<OldSheetsImportTask>() with provider { OldSheetsImportTask(kodein) }
    bind<SheetsExportTask>() with provider { SheetsExportTask() }
    bind<TelegramPullTask>() with provider { TelegramPullTask(kodein) }
    bind<CurrencyPricesUpdateTask>() with provider { CurrencyPricesUpdateTask(kodein) }
}
