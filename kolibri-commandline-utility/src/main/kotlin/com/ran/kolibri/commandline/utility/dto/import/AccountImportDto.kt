package com.ran.kolibri.commandline.utility.dto.import

import com.ran.kolibri.common.entity.enums.AccountType
import com.ran.kolibri.common.entity.enums.Currency
import java.math.BigDecimal
import org.joda.time.DateTime

data class AccountImportDto(
    val importName: String,
    val name: String,
    val type: AccountType,
    val currency: Currency?,
    val financialAssetImportDto: FinancialAssetImportDto?,
    val finalAmount: BigDecimal?,
    val createDate: DateTime,
    val isClosed: Boolean
)
