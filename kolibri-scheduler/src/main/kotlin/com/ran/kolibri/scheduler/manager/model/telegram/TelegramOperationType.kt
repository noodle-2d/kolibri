package com.ran.kolibri.scheduler.manager.model.telegram

enum class TelegramOperationType(val operationName: String) {
    IMPORT_OLD_SHEETS("/import_old_sheets"),
    SHOW_ACCOUNTS_STAT("/show_accounts_stat"),
    ADD_TRANSACTION("/add_transaction")
}
