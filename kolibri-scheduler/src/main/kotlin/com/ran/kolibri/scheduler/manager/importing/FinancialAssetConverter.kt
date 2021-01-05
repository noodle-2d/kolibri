package com.ran.kolibri.scheduler.manager.importing

import com.ran.kolibri.common.client.sheets.model.SheetRow
import com.ran.kolibri.common.entity.FinancialAsset
import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.common.entity.enums.FinancialAssetType
import java.lang.IllegalArgumentException

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
            ?: tryExtractRegex(name, OPTION_NAME_REGEX)
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
            contains(name, FUND_SET) -> FinancialAssetType.FUND
            contains(name, STOCK_SET) -> FinancialAssetType.STOCK
            contains(name, BOND_SET) -> FinancialAssetType.BOND
            contains(name, OPTION_SET) -> FinancialAssetType.OPTION
            else -> throw IllegalArgumentException("Unknown financial asset type for $name")
        }

    private fun evaluateCurrency(currencyString: String): Currency =
        when (currencyString) {
            "$" -> Currency.USD
            "Р" -> Currency.RUB
            "E", "Е" -> Currency.EUR
            else -> throw IllegalArgumentException("Unknown currency $currencyString")
        }

    private val STOCK_NAME_REGEX = Regex("^Акции (.*)$")
    private val BOND_NAME_REGEX = Regex("^Облигации (.*)$")
    private val FUND_NAME_REGEX = Regex("^Фонд (\\S*) .*$")
    private val OPTION_NAME_REGEX = Regex("^Опционы (.*)$")

    private val STOCK_SET = setOf("акции")
    private val BOND_SET = setOf("облигации", "офз")
    private val FUND_SET = setOf("фонд")
    private val OPTION_SET = setOf("опционы")
}
