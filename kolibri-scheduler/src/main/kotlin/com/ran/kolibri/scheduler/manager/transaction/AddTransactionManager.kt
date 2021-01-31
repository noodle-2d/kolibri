package com.ran.kolibri.scheduler.manager.transaction

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.client.telegram.model.Update
import com.ran.kolibri.common.entity.enums.TransactionType
import com.ran.kolibri.common.manager.ChatContext
import com.ran.kolibri.common.manager.Ignored
import com.ran.kolibri.common.manager.ListOption
import com.ran.kolibri.common.manager.Processed
import com.ran.kolibri.common.manager.ProcessingInContextResult
import com.ran.kolibri.common.manager.TelegramManager

class AddTransactionManager(kodein: Kodein) {

    private val telegramManager: TelegramManager = kodein.instance()

    suspend fun startAddingTransaction() =
        telegramManager.doActionSettingChatContext {
            val messageId = telegramManager
                .sendMessageToOwner("Choose type of transaction to add.", CHOOSE_TRANSACTION_TYPE_OPTIONS)
            ChooseTransactionTypeContext(messageId)
        }

    suspend fun processAddingTransactionInContext(
        context: AddTransactionContext,
        update: Update
    ): ProcessingInContextResult =
        when (context) {
            is ChooseTransactionTypeContext -> processChooseTransactionContext(context, update)
            else -> Ignored
        }

    private suspend fun processChooseTransactionContext(
        context: ChooseTransactionTypeContext,
        update: Update
    ): ProcessingInContextResult =
        if (update.callbackQuery?.message?.messageId != context.messageId) {
            Ignored
        } else when (update.callbackQuery?.data) {
            TransactionType.INCOME.name -> startAddingIncomeTransaction(context.messageId)
            TransactionType.EXPENSE.name -> startAddingExpenseTransaction(context.messageId)
            TransactionType.TRANSFER.name -> startAddingTransferTransaction(context.messageId)
            TransactionType.CURRENCY_CONVERSION.name -> startAddingCurrencyConversionTransaction(context.messageId)
            TransactionType.FINANCIAL_ASSET_PURCHASE.name -> startAddingFinancialAssetTransaction(context.messageId)
            TransactionType.FINANCIAL_ASSET_SALE.name -> startAddingFinancialAssetTransaction(context.messageId)
            else -> Ignored
        }

    private suspend fun startAddingIncomeTransaction(messageId: Int): ProcessingInContextResult {
        telegramManager.editMessageToOwner(messageId, "Adding income transaction. (todo)")
        return Processed(null)
    }

    private suspend fun startAddingExpenseTransaction(messageId: Int): ProcessingInContextResult {
        telegramManager.editMessageToOwner(messageId, "Adding expense transaction. (todo)")
        return Processed(null)
    }

    private suspend fun startAddingTransferTransaction(messageId: Int): ProcessingInContextResult {
        telegramManager.editMessageToOwner(messageId, "Adding transfer transaction. (todo)")
        return Processed(null)
    }

    private suspend fun startAddingFinancialAssetTransaction(messageId: Int): ProcessingInContextResult {
        telegramManager.editMessageToOwner(messageId, "Adding financial asset transaction. (todo)")
        return Processed(null)
    }

    private suspend fun startAddingCurrencyConversionTransaction(messageId: Int): ProcessingInContextResult {
        telegramManager.editMessageToOwner(messageId, "Adding currency conversion transaction. (todo)")
        return Processed(null)
    }

    companion object {
        private val CHOOSE_TRANSACTION_TYPE_OPTIONS = listOf(
            "Income transaction" to TransactionType.INCOME,
            "Expense transaction" to TransactionType.EXPENSE,
            "Transfer transaction" to TransactionType.TRANSFER,
            "Financial asset purchase" to TransactionType.FINANCIAL_ASSET_PURCHASE,
            "Financial asset sale" to TransactionType.FINANCIAL_ASSET_SALE,
            "Currency conversion" to TransactionType.CURRENCY_CONVERSION
        ).map { ListOption(it.first, it.second.name) }
    }
}

interface AddTransactionContext : ChatContext

data class ChooseTransactionTypeContext(override val messageId: Int) : AddTransactionContext

// todo: supplement by all necessary fields
data class AddIncomeTransactionContext(
    override val messageId: Int
) : AddTransactionContext

data class AddExpenseTransactionContext(
    override val messageId: Int
) : AddTransactionContext

data class AddTransferTransactionContext(
    override val messageId: Int
) : AddTransactionContext

data class AddCurrencyConversionTransactionContext(
    override val messageId: Int
) : AddTransactionContext

data class AddFinancialAssetTransactionContext(
    override val messageId: Int
) : AddTransactionContext
