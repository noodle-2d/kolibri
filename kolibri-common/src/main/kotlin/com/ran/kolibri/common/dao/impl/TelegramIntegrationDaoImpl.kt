package com.ran.kolibri.common.dao.impl

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dao.TelegramIntegrationDao
import com.ran.kolibri.common.entity.TelegramIntegration
import com.ran.kolibri.common.util.runTransaction
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class TelegramIntegrationDaoImpl(kodein: Kodein) : TelegramIntegrationDao {

    private val db: Database = kodein.instance()

    override suspend fun get(): TelegramIntegration =
        runTransaction(db) {
            TelegramIntegrations.selectAll().map { mapRow(it) }
        }.first()

    override suspend fun update(integration: TelegramIntegration): Int =
        runTransaction(db) {
            TelegramIntegrations.update {
                it[lastUpdateId] = integration.lastUpdateId
            }
        }

    private fun mapRow(row: ResultRow): TelegramIntegration =
        TelegramIntegration(row[TelegramIntegrations.lastUpdateId])
}

object TelegramIntegrations : Table("telegram_integration") {
    val lastUpdateId = long("last_update_id")
}
