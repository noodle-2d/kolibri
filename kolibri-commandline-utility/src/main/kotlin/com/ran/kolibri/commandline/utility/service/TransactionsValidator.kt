package com.ran.kolibri.commandline.utility.service

import com.ran.kolibri.commandline.utility.dto.import.AccountImportDto
import com.ran.kolibri.commandline.utility.dto.import.TransactionImportDto
import com.ran.kolibri.common.util.log

object TransactionsValidator {

    fun validate(transactions: List<TransactionImportDto>, accounts: List<AccountImportDto>) {
        log.info("Validating transactions") // todo
    }
}
