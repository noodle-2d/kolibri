package com.ran.kolibri.common.dao

import com.ran.kolibri.common.entity.TelegramIntegration

interface TelegramIntegrationDao {
    suspend fun get(): TelegramIntegration
    suspend fun update(integration: TelegramIntegration): Int
}
