package com.ran.kolibri.common.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.TextColumnType
import org.jetbrains.exposed.sql.statements.api.PreparedStatementApi
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.postgresql.util.PGobject
import java.math.BigDecimal
import kotlin.reflect.KClass

fun executeSqlStatement(sql: String) {
    val conn = TransactionManager.current().connection
    val statement = conn.prepareStatement(sql, emptyArray())
    statement.executeUpdate()
}

interface PgTypeDescriptor<T : Enum<T>> {
    val klass: KClass<T>
    val values: Array<T>
    val sqlTypeName: String
}

fun <T : Enum<T>> Table.pgEnum(name: String, descriptor: PgTypeDescriptor<T>): Column<T> =
    customEnumeration(
        name,
        descriptor.sqlTypeName,
        { dbValue -> descriptor.values.first { it.name.lowercase() == dbValue } },
        { enumValue: T -> PGobject().apply { type = descriptor.sqlTypeName; value = enumValue.name.lowercase() } }
    )

fun Table.decimal(name: String): Column<BigDecimal> =
    decimal(name, DEFAULT_DECIMAL_PRECISION, DEFAULT_DECIMAL_SCALE)

private const val DEFAULT_DECIMAL_PRECISION = 22
private const val DEFAULT_DECIMAL_SCALE = 4

inline fun <reified T : Any> Table.json(name: String): Column<T> =
    registerColumn(name, JsonColumnType(T::class.java))

class JsonColumnType<T : Any>(private val klass: Class<T>) : TextColumnType() {

    override fun valueFromDB(value: Any): T {
        val stringValue = super.valueFromDB(value).toString()
        return JSON_MAPPER.readValue(stringValue, klass)
    }

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val stringValue = value?.let { JSON_MAPPER.writeValueAsString(it) }
        super.setParameter(stmt, index, stringValue)
    }

    companion object {
        private val JSON_MAPPER = ObjectMapper().apply {
            registerModule(JodaModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }
}
