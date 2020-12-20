package com.ran.kolibri.common.dao

import com.ran.kolibri.common.dao.model.AggregatedAccount
import com.ran.kolibri.common.entity.Account

interface AccountDao {
    suspend fun insertAccounts(accounts: List<Account>): List<Account>
    suspend fun selectAll(): List<Account>
    suspend fun aggregateActiveAccounts(): List<AggregatedAccount>
    suspend fun deleteAllAccounts(): Int
    suspend fun restartIdSequence()
}
