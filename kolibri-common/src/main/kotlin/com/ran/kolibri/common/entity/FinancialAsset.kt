package com.ran.kolibri.common.entity

import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.common.entity.enums.FinancialAssetType

data class FinancialAsset(
    val id: Long? = null,
    val name: String,
    val companyName: String,
    val type: FinancialAssetType,
    val currency: Currency
)
