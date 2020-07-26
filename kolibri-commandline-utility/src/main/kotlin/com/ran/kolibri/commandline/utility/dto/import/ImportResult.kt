package com.ran.kolibri.commandline.utility.dto.import

import com.ran.kolibri.commandline.utility.dto.action.ActionResult

data class ImportResult(
    val transactionsCount: Int,
    val accountsCount: Int,
    val financialAssetsCount: Int
) : ActionResult {

    override fun asString(): String =
        "$transactionsCount transactions, $accountsCount accounts and " +
            "$financialAssetsCount financial assets were successfully imported."
}
