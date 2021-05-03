package com.ran.kolibri.common.dao

import com.ran.kolibri.common.entity.TelegramOperation

interface TelegramOperationDao {
    suspend fun insert(operation: TelegramOperation): TelegramOperation
    suspend fun findByMessageId(messageId: Int): TelegramOperation?
    suspend fun findLast(): TelegramOperation?
    suspend fun update(operation: TelegramOperation): Int
}
