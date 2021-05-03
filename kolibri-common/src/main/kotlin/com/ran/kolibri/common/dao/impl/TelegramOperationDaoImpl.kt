package com.ran.kolibri.common.dao.impl

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dao.TelegramOperationDao
import com.ran.kolibri.common.entity.TelegramOperation
import com.ran.kolibri.common.entity.telegram.operation.context.TelegramOperationContext
import com.ran.kolibri.common.util.json
import com.ran.kolibri.common.util.runTransaction
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class TelegramOperationDaoImpl(kodein: Kodein) : TelegramOperationDao {

    private val db: Database = kodein.instance()

    override suspend fun insert(operation: TelegramOperation): TelegramOperation =
        runTransaction(db) {
            val transactionOperationId = TelegramOperations.insert {
                it[operationName] = operation.operationName
                it[messageId] = operation.messageId
                it[context] = operation.context
                it[createTime] = operation.createTime
            } get TelegramOperations.id
            operation.copy(id = transactionOperationId.value)
        }

    override suspend fun findByMessageId(messageId: Int): TelegramOperation? =
        runTransaction(db) {
            TelegramOperations
                .select { TelegramOperations.messageId eq messageId }
                .orderBy(TelegramOperations.id, SortOrder.DESC)
                .limit(1)
                .map { mapRow(it) }
                .firstOrNull()
        }

    override suspend fun findLast(): TelegramOperation? =
        runTransaction(db) {
            val maxId = TelegramOperations
                .slice(TelegramOperations.id.max())
                .selectAll()
            TelegramOperations
                .select { TelegramOperations.id inSubQuery maxId }
                .map { mapRow(it) }
                .firstOrNull()
        }

    override suspend fun update(operation: TelegramOperation): Int =
        runTransaction(db) {
            TelegramOperations.update({ TelegramOperations.id eq operation.id }) {
                it[messageId] = operation.messageId
                it[context] = operation.context
            }
        }

    private fun mapRow(row: ResultRow): TelegramOperation =
        TelegramOperation(
            id = row[TelegramOperations.id].value,
            operationName = row[TelegramOperations.operationName],
            messageId = row[TelegramOperations.messageId],
            context = row[TelegramOperations.context],
            createTime = row[TelegramOperations.createTime]
        )
}

object TelegramOperations : LongIdTable("telegram_operation") {
    val operationName = varchar("operation_name", 100)
    val messageId = integer("message_id").nullable()
    val context = json<TelegramOperationContext>("context").nullable()
    val createTime = datetime("create_time")
}
