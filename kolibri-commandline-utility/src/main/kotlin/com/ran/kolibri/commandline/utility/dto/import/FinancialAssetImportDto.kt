package com.ran.kolibri.commandline.utility.dto.import

import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.common.entity.enums.FinancialAssetType

data class FinancialAssetImportDto(
    val importName: String,
    val name: String,
    val companyName: String,
    val type: FinancialAssetType,
    val currency: Currency
)
