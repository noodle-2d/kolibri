package com.ran.kolibri.commandline.utility.service

import com.ran.kolibri.common.dto.sheets.SheetRow
import com.ran.kolibri.common.entity.Account
import com.ran.kolibri.common.entity.enums.AccountType
import com.ran.kolibri.common.entity.enums.Currency
import java.math.BigDecimal
import org.joda.time.DateTime

object AccountConverter {

    // todo: add transactions list argument, evaluate initial amount, create and close dates
    fun convert(row: SheetRow): Account {
        val name = evaluateName(row.values.first())
        return Account(
            name = name,
            type = evaluateAccountType(name),
            currency = evaluateCurrency(name),
            financialAssetId = null,
            initialAmount = BigDecimal.ZERO,
            createDate = DateTime.now().withTimeAtStartOfDay(),
            closeDate = null
        )
    }

    private fun evaluateName(rowValue: String): String =
        NAME_REGEX.find(rowValue)!!.groupValues.last()

    private fun evaluateAccountType(name: String): AccountType =
        when {
            contains(name, CASH_SET) -> AccountType.CASH
            contains(name, DEPOSIT_SET) -> AccountType.DEPOSIT
            contains(name, DEPT_SET) -> AccountType.DEPT
            contains(name, CREDIT_CARD_SET) -> AccountType.CREDIT_CARD
            contains(name, DEBIT_CARD_SET) -> AccountType.DEBIT_CARD
            contains(name, FINANCIAL_ASSETS_SET) -> AccountType.FINANCIAL_ASSET
            else -> AccountType.OTHER
        }

    private fun evaluateCurrency(name: String): Currency? =
        when {
            contains(name, FINANCIAL_ASSETS_SET) -> null
            contains(name, USD_SET) -> Currency.USD
            contains(name, EUR_SET) -> Currency.EUR
            contains(name, CNY_SET) -> Currency.CNY
            contains(name, BTC_SET) -> Currency.BTC
            contains(name, GBR_SET) -> Currency.GBR
            contains(name, CZK_SET) -> Currency.CZK
            else -> Currency.RUB
        }

    private fun contains(string: String, substringsSet: Set<String>): Boolean =
        substringsSet.any { string.toLowerCase().contains(it) }

    private val NAME_REGEX = Regex("""^\d*\. (.*)$""")
    private val FINANCIAL_ASSETS_SET = setOf("акции", "облигации", "офз", "вечный портфель")
    private val USD_SET = setOf("доллары")
    private val EUR_SET = setOf("евро")
    private val CNY_SET = setOf("юани")
    private val BTC_SET = setOf("биткоины")
    private val GBR_SET = setOf("фунты стерлингов")
    private val CZK_SET = setOf("чешские кроны")
    private val CASH_SET = setOf("в кармане", "на сохранении")
    private val DEPOSIT_SET = setOf("вклад")
    private val DEPT_SET = setOf("долг")
    private val CREDIT_CARD_SET = setOf("кредитная карта")
    private val DEBIT_CARD_SET = setOf("карта")
}
