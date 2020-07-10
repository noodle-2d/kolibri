package com.ran.kolibri.common.util

import java.math.BigDecimal
import kotlin.reflect.KClass
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

interface PgTypeDescriptor<T : Enum<T>> {
    val klass: KClass<T>
    val values: Array<T>
    val sqlTypeName: String
}

fun <T : Enum<T>> Table.pgEnum(name: String, descriptor: PgTypeDescriptor<T>): Column<T> =
    customEnumeration(
        name,
        descriptor.sqlTypeName,
        { dbValue -> descriptor.values.first { it.name.toLowerCase() == dbValue } },
        { enumValue: T -> PGobject().apply { type = descriptor.sqlTypeName; value = enumValue.name.toLowerCase() } }
    )

fun Table.decimal(name: String): Column<BigDecimal> =
    decimal(name, DEFAULT_DECIMAL_PRECISION, DEFAULT_DECIMAL_SCALE)

private const val DEFAULT_DECIMAL_PRECISION = 22
private const val DEFAULT_DECIMAL_SCALE = 4
