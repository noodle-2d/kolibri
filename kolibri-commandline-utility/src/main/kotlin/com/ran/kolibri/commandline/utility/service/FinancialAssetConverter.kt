package com.ran.kolibri.commandline.utility.service

import com.ran.kolibri.common.dto.sheets.SheetRow
import com.ran.kolibri.common.entity.FinancialAsset
import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.common.entity.enums.FinancialAssetType

object FinancialAssetConverter : ConverterUtils {

    fun convert(row: SheetRow): FinancialAsset {
        val name = row.values.first()
        return FinancialAsset(
            name = name,
            companyName = evaluateCompanyName(name),
            type = evaluateType(name),
            currency = evaluateCurrency(row.values.last())
        )
    }

    private fun evaluateCompanyName(name: String): String {
        val extractedName = tryExtractRegex(name, STOCK_NAME_REGEX)
            ?: tryExtractRegex(name, BOND_NAME_REGEX)
            ?: tryExtractRegex(name, FUND_NAME_REGEX)
            ?: name

        return when (extractedName) {
            "НорНикеля" -> "НорНикель"
            "ПИКа" -> "ПИК"
            "ОФЗ", "России USD" -> "Россия"
            "Сбербанка" -> "Сбербанк"
            "Евроторга" -> "Евроторг"
            else -> extractedName
        }
    }

    private fun tryExtractRegex(name: String, regex: Regex): String? =
        regex.find(name)?.groupValues?.last()

    private fun evaluateType(name: String): FinancialAssetType =
        when {
            contains(name, STOCK_SET) -> FinancialAssetType.STOCK
            contains(name, BOND_SET) -> FinancialAssetType.BOND
            else -> FinancialAssetType.FUND
        }

    private fun evaluateCurrency(currencyString: String): Currency =
        when (currencyString) {
            "$" -> Currency.USD
            "Р" -> Currency.RUB
            else -> Currency.EUR
        }

    private val STOCK_NAME_REGEX = Regex("^Акции (.*)$")
    private val BOND_NAME_REGEX = Regex("^Облигации (.*)$")
    private val FUND_NAME_REGEX = Regex("^Вечный портфель (.*) .*$")

    private val STOCK_SET = setOf("акции")
    private val BOND_SET = setOf("облигации", "офз")
}
