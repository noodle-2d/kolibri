package com.ran.kolibri.scheduler.manager.transaction

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.entity.TelegramOperation
import com.ran.kolibri.common.entity.enums.TransactionType
import com.ran.kolibri.common.entity.telegram.operation.context.AddTransactionContext
import com.ran.kolibri.common.entity.telegram.operation.context.ExpenseTransactionInfo
import com.ran.kolibri.common.entity.telegram.operation.context.IncomeTransactionInfo
import com.ran.kolibri.common.entity.telegram.operation.context.TelegramOperationContext
import com.ran.kolibri.common.manager.ListOption
import com.ran.kolibri.common.manager.TelegramManager
import com.ran.kolibri.scheduler.manager.IgnoreUpdateProcessor
import com.ran.kolibri.scheduler.manager.TelegramUpdateProcessor
import com.ran.kolibri.scheduler.manager.model.telegram.CallbackQuery
import com.ran.kolibri.scheduler.manager.model.telegram.OperationContinuation
import com.ran.kolibri.scheduler.manager.model.telegram.OperationInitiation
import com.ran.kolibri.scheduler.manager.model.telegram.OperationUpdate
import com.ran.kolibri.scheduler.manager.model.telegram.TelegramOperationType

class AddTransactionManager(kodein: Kodein) : TelegramUpdateProcessor {

    private val telegramManager: TelegramManager = kodein.instance()
    private val financialAssetTransactionManager: AddFinancialAssetTransactionManager = kodein.instance()

    override val operationType: TelegramOperationType
        get() = TelegramOperationType.ADD_TRANSACTION

    override suspend fun processUpdate(operation: TelegramOperation, update: OperationUpdate): TelegramOperation =
        when (update) {
            is OperationInitiation -> startAddingTransaction(operation)
            is OperationContinuation -> processAddingTransactionContinuation(operation, update)
            else -> IgnoreUpdateProcessor.processUpdate(operation, update)
        }

    private suspend fun startAddingTransaction(operation: TelegramOperation): TelegramOperation {
        val messageId = telegramManager
            .sendMessageToOwner("Choose type of transaction to add.", CHOOSE_TRANSACTION_TYPE_OPTIONS)
        val context = TelegramOperationContext(
            addTransaction = AddTransactionContext()
        )
        return operation.copy(
            messageId = messageId,
            context = context
        )
    }

    private suspend fun processAddingTransactionContinuation(
        operation: TelegramOperation,
        update: OperationUpdate
    ): TelegramOperation =
        when {
            operation.context?.addTransaction?.income != null -> processIncomeTransaction(operation, update)
            operation.context?.addTransaction?.expense != null -> processExpenseTransaction(operation, update)
            else -> processChooseTransaction(operation, update)
        }

    private suspend fun processIncomeTransaction(
        operation: TelegramOperation,
        update: OperationUpdate
    ): TelegramOperation =
        IgnoreUpdateProcessor.processUpdate(operation, update) // todo

    private suspend fun processExpenseTransaction(
        operation: TelegramOperation,
        update: OperationUpdate
    ): TelegramOperation =
        IgnoreUpdateProcessor.processUpdate(operation, update) // todo

    private suspend fun processChooseTransaction(
        operation: TelegramOperation,
        update: OperationUpdate
    ): TelegramOperation =
        if (update is CallbackQuery) {
            when (update.data) {
                TransactionType.INCOME.name -> startAddingIncomeTransaction(operation, update)
                TransactionType.EXPENSE.name -> startAddingExpenseTransaction(operation, update)
                TransactionType.TRANSFER.name -> startAddingTransferTransaction(operation, update)
                TransactionType.CURRENCY_CONVERSION.name -> startAddingCurrencyConversionTransaction(operation, update)
                TransactionType.FINANCIAL_ASSET_PURCHASE.name ->
                    financialAssetTransactionManager.startAddingFinancialAssetTransaction(operation, update)
                TransactionType.FINANCIAL_ASSET_SALE.name ->
                    financialAssetTransactionManager.startAddingFinancialAssetTransaction(operation, update)
                else -> IgnoreUpdateProcessor.processUpdate(operation, update)
            }
        } else IgnoreUpdateProcessor.processUpdate(operation, update)

    private suspend fun startAddingIncomeTransaction(
        operation: TelegramOperation,
        update: OperationUpdate
    ): TelegramOperation {
        telegramManager.editMessageToOwner(operation.messageId!!, "Adding income transaction. (todo)")
        val addTransactionContext = AddTransactionContext(income = IncomeTransactionInfo())
        val context = operation.context!!.copy(addTransaction = addTransactionContext)
        return operation.copy(context = context)
    }

    private suspend fun startAddingExpenseTransaction(
        operation: TelegramOperation,
        update: OperationUpdate
    ): TelegramOperation {
        telegramManager.editMessageToOwner(operation.messageId!!, "Adding expense transaction. (todo)")
        val addTransactionContext = AddTransactionContext(expense = ExpenseTransactionInfo())
        val context = operation.context!!.copy(addTransaction = addTransactionContext)
        return operation.copy(context = context)
    }

    private suspend fun startAddingTransferTransaction(
        operation: TelegramOperation,
        update: OperationUpdate
    ): TelegramOperation {
        telegramManager.editMessageToOwner(operation.messageId!!, "Adding transfer transaction. (todo)")
        return operation
    }

    private suspend fun startAddingCurrencyConversionTransaction(
        operation: TelegramOperation,
        update: OperationUpdate
    ): TelegramOperation {
        telegramManager.editMessageToOwner(operation.messageId!!, "Adding currency conversion transaction. (todo)")
        return operation
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
