package com.ran.kolibri.commandline.utility.service

import com.ran.kolibri.commandline.utility.dto.import.AccountImportDto
import com.ran.kolibri.commandline.utility.dto.import.TransactionImportDto
import com.ran.kolibri.common.dto.sheets.SheetRow
import com.ran.kolibri.common.entity.Account
import com.ran.kolibri.common.entity.FinancialAsset
import com.ran.kolibri.common.entity.enums.AccountType
import com.ran.kolibri.common.entity.enums.Currency
import java.math.BigDecimal
import org.joda.time.DateTime

object AccountConverter : ConverterUtils {

    fun convertToImportDto(row: SheetRow, financialAssets: List<FinancialAsset>): AccountImportDto {
        val importName = row.values.first()
        val finalAmountString = row.values.last()
            .let { if (it == "---") null else it }
        val finalAmount = finalAmountString?.let { bigDecimal(it) }

        val name = evaluateName(importName)
        val type = evaluateAccountType(name)
        val currency = evaluateCurrency(name)
        val isClosed = finalAmount == null
        val financialAsset = financialAssets.find { it.name == name }

        return AccountImportDto(importName, name, type, currency, financialAsset, finalAmount, isClosed)
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

    fun convert(importDto: AccountImportDto, transactions: List<TransactionImportDto>): Account =
        Account(
            name = importDto.name,
            type = importDto.type,
            currency = importDto.currency,
            financialAssetId = importDto.financialAsset?.id,
            initialAmount = BigDecimal.ZERO, // todo: evaluate it by transactions list
            createDate = DateTime.now().withTimeAtStartOfDay(), // todo: evaluate it by transactions list
            closeDate = if (importDto.isClosed) findLastTransactionDate(transactions, importDto.importName) else null
        )

    private fun findLastTransactionDate(transactions: List<TransactionImportDto>, accountName: String): DateTime =
        transactions
            .filter { it.accountString == accountName }
            .map { it.date }
            .max()!!

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
