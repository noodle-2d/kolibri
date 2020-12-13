package com.ran.kolibri.common.dao

import com.ran.kolibri.common.entity.Transaction

interface TransactionDao {
    suspend fun insertTransactions(transactions: List<Transaction>): List<Transaction>
    suspend fun updateTransaction(transaction: Transaction): Int
    suspend fun deleteAllTransactions(): Int
    suspend fun restartIdSequence()
}
