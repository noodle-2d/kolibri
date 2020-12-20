package com.ran.kolibri.scheduler.manager.importing

import com.ran.kolibri.common.client.sheets.model.SheetRow
import com.ran.kolibri.common.entity.Account
import com.ran.kolibri.common.entity.FinancialAsset
import com.ran.kolibri.common.entity.enums.AccountType
import com.ran.kolibri.common.entity.enums.Currency
import com.ran.kolibri.scheduler.manager.importing.model.AccountImportDto
import com.ran.kolibri.scheduler.manager.importing.model.TransactionImportDto
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
            contains(name, STRANGER_SET) -> AccountType.STRANGER
            contains(name, CASH_SET) -> AccountType.CASH
            contains(name, DEPOSIT_SET) -> AccountType.DEPOSIT
            contains(name, DEPT_SET) -> AccountType.DEPT
            contains(name, CREDIT_CARD_SET) -> AccountType.CREDIT_CARD
            contains(name, DEBIT_CARD_SET) -> AccountType.DEBIT_CARD
            contains(name, BROKER_SET) -> AccountType.BROKER
            contains(name, BTC_SET) -> AccountType.CRYPTO
            contains(name, FINANCIAL_ASSETS_SET) -> AccountType.FINANCIAL_ASSET
            else -> throw IllegalArgumentException("Unknown account type for $name")
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
            createDate = findFirstTransactionDate(transactions, importDto.importName),
            closeDate = if (importDto.isClosed) findLastTransactionDate(transactions, importDto.importName) else null
        )

    private fun findFirstTransactionDate(transactions: List<TransactionImportDto>, accountName: String): DateTime =
        transactions
            .filter { it.accountString == accountName }
            .map { it.date }
            .min()!!

    private fun findLastTransactionDate(transactions: List<TransactionImportDto>, accountName: String): DateTime =
        transactions
            .filter { it.accountString == accountName }
            .map { it.date }
            .max()!!

    private val NAME_REGEX = Regex("""^\d*\. (.*)$""")

    private val USD_SET = setOf("доллары")
    private val EUR_SET = setOf("евро")
    private val CNY_SET = setOf("юани")
    private val BTC_SET = setOf("биткоины")
    private val GBR_SET = setOf("фунты стерлингов")
    private val CZK_SET = setOf("чешские кроны")

    private val STRANGER_SET = setOf("на сохранении", "в кармане (отца)", "в кармане (бабушки и деда)")
    private val CASH_SET = setOf("в кармане", "яндекс.деньги")
    private val DEPOSIT_SET = setOf("вклад")
    private val DEPT_SET = setOf("долг")
    private val CREDIT_CARD_SET = setOf("кредитная карта")
    private val DEBIT_CARD_SET = setOf("карта")
    private val BROKER_SET = setOf("брокерский")
    private val FINANCIAL_ASSETS_SET = setOf("акции", "облигации", "офз", "вечный портфель", "фонд", "опционы")
}
