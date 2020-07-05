package com.ran.kolibri.common.entity

import com.ran.kolibri.common.entity.enums.AccountType
import com.ran.kolibri.common.entity.enums.Currency
import java.math.BigDecimal
import org.joda.time.DateTime

data class Account(
    val id: Long? = null,
    val name: String,
    val type: AccountType,
    val currency: Currency?,
    val financialAssetId: Long?,
    val initialAmount: BigDecimal,
    val createDate: DateTime,
    val closeDate: DateTime?
)
