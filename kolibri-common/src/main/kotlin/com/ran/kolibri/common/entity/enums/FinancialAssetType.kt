package com.ran.kolibri.common.entity.enums

import com.ran.kolibri.common.util.PgTypeDescriptor
import kotlin.reflect.KClass

enum class FinancialAssetType {
    STOCK,
    BOND,
    FUND,
    OPTION;

    companion object : PgTypeDescriptor<FinancialAssetType> {
        override val klass: KClass<FinancialAssetType> = FinancialAssetType::class
        override val values: Array<FinancialAssetType> = values()
        override val sqlTypeName: String = "financial_asset_type"
    }
}
