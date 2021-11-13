package com.ran.kolibri.scheduler.manager.importing

import com.ran.kolibri.common.client.sheets.model.SheetRow
import com.ran.kolibri.common.entity.Account
import com.ran.kolibri.common.entity.Transaction
import com.ran.kolibri.common.entity.enums.AccountType
import com.ran.kolibri.common.entity.enums.ExternalTransactionCategory
import com.ran.kolibri.common.entity.enums.TransactionType
import com.ran.kolibri.common.util.log
import com.ran.kolibri.scheduler.manager.importing.model.TransactionImportDto
import java.lang.RuntimeException
import java.math.BigDecimal
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object TransactionConverter : ConverterUtils {

    fun convertToImportDto(row: SheetRow): TransactionImportDto {
        log.info("Converting transaction row $row")
        val accountString = row.values.first()
        val resultAmount = bigDecimal(row.values[2])
        val amount = evaluateAmount(row.values[1], resultAmount)
        val comment = row.values[3]
        val date = DateTime.parse(row.values[4], DATE_FORMATTER)
        val transactionType = evaluateTransactionType(comment, amount)

        val (exactFinancialAssetPrice, exactSoldCurrencyRatioPart, exactBoughtCurrencyRatioPart) =
            (if (row.values.size == 6) row.values[5] else null)
                ?.let { evaluateExactCourseValues(it, comment) }
                ?: Triple(null, null, null)

        return TransactionImportDto(
            accountString,
            transactionType,
            amount,
            resultAmount,
            date,
            comment,
            exactFinancialAssetPrice,
            exactSoldCurrencyRatioPart,
            exactBoughtCurrencyRatioPart
        )
    }

    private fun evaluateAmount(amountString: String, resultAmount: BigDecimal): BigDecimal =
        if (amountString == "") resultAmount else bigDecimal(amountString)

    private fun evaluateTransactionType(comment: String, amount: BigDecimal): TransactionType =
        when {
            contains(comment, FINANCIAL_ASSET_SINGS) && contains(comment, PURCHASE_SIGNS) ->
                TransactionType.FINANCIAL_ASSET_PURCHASE
            contains(comment, FINANCIAL_ASSET_SINGS) && contains(comment, SALE_SIGNS) &&
                !contains(comment, setOf("ford")) ->
                TransactionType.FINANCIAL_ASSET_SALE
            contains(comment, CURRENCY_CONVERSION_SIGNS) && !contains(comment, NOT_CURRENCY_CONVERSION_SIGNS) ->
                TransactionType.CURRENCY_CONVERSION
            contains(comment, TRANSFER_SIGNS) ->
                TransactionType.TRANSFER
            amount > BigDecimal.ZERO && !contains(comment, INCOME_EXCLUSIONS) ->
                TransactionType.INCOME
            else ->
                TransactionType.EXPENSE
        }

    private fun evaluateExactCourseValues(
        courseString: String,
        comment: String
    ): Triple<BigDecimal?, BigDecimal?, BigDecimal?>? =
        COURSE_NUMBER_REGEX.find(courseString)?.let { match ->
            val courseCurrency = match.groupValues[1]
            val courseNumber = bigDecimal(match.groupValues[2])
            log.info("Course currency: $courseCurrency; course number: $courseNumber")
            validateExactCourseValues(courseCurrency, courseNumber, comment)

            when (courseCurrency) {
                "шт." -> Triple(courseNumber, null, null)
                else -> when {
                    contains(comment, PURCHASE_SIGNS) -> Triple(null, courseNumber, BigDecimal.ONE)
                    contains(comment, SALE_SIGNS) -> Triple(null, BigDecimal.ONE, courseNumber)
                    else ->
                        throw RuntimeException("Strange currency conversion: $courseCurrency, $courseNumber, $comment")
                }
            }
        }

    private fun validateExactCourseValues(
        courseCurrency: String,
        courseNumber: BigDecimal,
        comment: String
    ) {
        when (courseCurrency.trim()) {
            "шт." -> if (!contains(comment, FINANCIAL_ASSET_SINGS)) {
                throw RuntimeException("Strange financial asset course: $courseCurrency, $courseNumber, $comment")
            }
            "$", "E", "Е", "U", "B", "крона" -> if (!contains(comment, CURRENCY_CONVERSION_SIGNS)) {
                throw RuntimeException("Strange currency conversion values: $courseCurrency, $courseNumber, $comment")
            }
            else -> throw RuntimeException("Strange exact course values: $courseCurrency, $courseNumber, $comment")
        }
    }

    fun convert(importDto: TransactionImportDto, accounts: List<Account>): Transaction? {
        if (importDto.amount == BigDecimal.ZERO) {
            if (importDto.comment == "Начальные" || importDto.comment.startsWith("Закрытие")) {
                log.info("Ignoring zero transaction $importDto")
                return null
            } else {
                log.info("Strange transaction $importDto")
                throw RuntimeException("Strange transaction $importDto")
            }
        }

        log.info("Converting transaction: $importDto")
        val account = accounts.find { importDto.accountString.endsWith(it.name) }!!
        val transactionCategory = evaluateExternalTransactionCategory(importDto.comment, importDto.type, account)

        return Transaction(
            accountId = account.id!!,
            type = importDto.type,
            externalTransactionCategory = transactionCategory,
            amount = importDto.amount,
            date = importDto.date,
            comment = importDto.comment,
            associatedTransactionId = null,
            exactFinancialAssetPrice = importDto.exactFinancialAssetPrice,
            exactBoughtCurrencyRatioPart = importDto.exactBoughtCurrencyRatioPart,
            exactSoldCurrencyRatioPart = importDto.exactSoldCurrencyRatioPart
        )
    }

    private fun evaluateExternalTransactionCategory(
        comment: String,
        transactionType: TransactionType,
        account: Account
    ): ExternalTransactionCategory? =
        if (NON_CATEGORIZED_ACCOUNT_TYPES.contains(account.type)) null else
            when (transactionType) {
                TransactionType.INCOME -> evaluateIncomeTransactionCategory(comment)
                TransactionType.EXPENSE -> evaluateExpenseTransactionCategory(comment)
                else -> null
            }

    private fun evaluateIncomeTransactionCategory(comment: String): ExternalTransactionCategory? =
        when {
            contains(comment, NON_CATEGORIZED_INCOME_SIGNS) -> null
            contains(comment, ACTIVE_INCOME_SIGNS) -> ExternalTransactionCategory.ACTIVE_INCOME
            contains(comment, PASSIVE_INCOME_SIGNS) -> ExternalTransactionCategory.PASSIVE_INCOME
            contains(comment, GIFT_INCOME_SIGNS) -> ExternalTransactionCategory.GIFT_INCOME
            // else -> throw IllegalArgumentException("Unknown category for transaction $comment")
            else -> {
                log.info("Unknown category: $comment")
                null
            }
        }

    private fun evaluateExpenseTransactionCategory(comment: String): ExternalTransactionCategory? =
        when {
            contains(comment, FINANCIAL_EXPENSE_SIGNS) -> ExternalTransactionCategory.FINANCIAL_EXPENSE
            contains(comment, NON_FINANCIAL_EXPENSE_SIGNS) -> ExternalTransactionCategory.NON_FINANCIAL_EXPENSE
            // else -> throw IllegalArgumentException("Unknown category for transaction $comment")
            else -> {
                log.info("Unknown category: $comment")
                null
            }
        }

    private val DATE_FORMATTER = DateTimeFormat.forPattern("dd.MM.yy")
    private val COURSE_NUMBER_REGEX = Regex("""^\(курс 1(.*) [=~] ([0-9,]*).*\)$""")
    private val FINANCIAL_ASSET_SINGS = setOf("акц", "облигац", "офз")
    private val CURRENCY_CONVERSION_SIGNS = setOf("доллар", "евро", "юаней", "чешских крон", "биткоин")
    private val NOT_CURRENCY_CONVERSION_SIGNS = setOf("евроторг")
    private val TRANSFER_SIGNS = setOf(
        "перевод",
        "снятие",
        "отдано в долг",
        "возврат долга",
        "взято у отца в долг",
        "вывод",
        "внесение денег",
        "пополнение брокерского"
    )
    private val INCOME_EXCLUSIONS = setOf("возврат", "возвращено из", "возвращено с")

    private val SALE_SIGNS = setOf(
        "продажа",
        "перевод долларов в рубли",
        "перевод евро в рубли",
        "перевод биткоинов в рубли"
    )
    private val PURCHASE_SIGNS = setOf(
        "покупка",
        "перевод рублей в доллары",
        "перевод рублей в евро",
        "перевод рублей в биткоины"
    )

    private val NON_CATEGORIZED_ACCOUNT_TYPES = setOf(AccountType.STRANGER, AccountType.DEPT)
    private val NON_CATEGORIZED_INCOME_SIGNS = setOf("начальные", "довложение на начальном этапе")
    private val ACTIVE_INCOME_SIGNS = setOf("зарплата", "аванс", "премия", "стипендия", "грант")
    private val PASSIVE_INCOME_SIGNS = setOf("процент", "купоны", "дивиденды", "бонус по акции")
    private val GIFT_INCOME_SIGNS = setOf("подарок")

    private val FINANCIAL_EXPENSE_SIGNS = setOf(
        "смартфон",
        "подарок",
        "подарк",
        "в долг",
        "одежд",
        "обув",
        "билет",
        "взято с собой",
        "возвращено из",
        "возвращено с",
        "чистк",
        "ткани для кресла",
        "steam",
        "зарядк",
        "на операцию",
        "джинс",
        "люстр",
        "пицц",
        "лечение",
        "гладильн",
        "хостел",
        "мышк",
        "элице",
        "зонт",
        "самокат",
        "картридж",
        "книг",
        "цепочки",
        "штан",
        "кофт",
        "лекарств",
        "валаам",
        "очков",
        "ботин",
        "чехла",
        "перевозка вещей",
        "сковород",
        "ремонт",
        "mi band",
        "продуктов",
        "газовой плиты",
        "забег"
    )
    private val NON_FINANCIAL_EXPENSE_SIGNS = setOf(
        "карманные расходы",
        "штраф",
        "еда",
        "еды",
        "интернет",
        "деньги на телефон",
        "ресторан",
        "пожертвование",
        "комиссия",
        "плата за аренду квартиры",
        "обед",
        "удержание ндфл",
        "оплата коммунальных"
    )
}
