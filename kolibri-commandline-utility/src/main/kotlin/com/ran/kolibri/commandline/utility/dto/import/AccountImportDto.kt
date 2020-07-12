package com.ran.kolibri.commandline.utility.dto.import

import com.ran.kolibri.common.entity.FinancialAsset
import com.ran.kolibri.common.entity.enums.AccountType
import com.ran.kolibri.common.entity.enums.Currency
import java.math.BigDecimal

data class AccountImportDto(
    val importName: String,
    val name: String,
    val type: AccountType,
    val currency: Currency?,
    val financialAsset: FinancialAsset?,
    val finalAmount: BigDecimal?,
    val isClosed: Boolean
)
