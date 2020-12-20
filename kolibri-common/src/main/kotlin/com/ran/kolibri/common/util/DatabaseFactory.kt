package com.ran.kolibri.common.util

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.config.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

fun buildDatabase(kodein: Kodein): Database {
    val databaseConfig = kodein.instance<DatabaseConfig>()
    val hikariConfig = mapToHikariConfig(databaseConfig)
    val dataSource = HikariDataSource(hikariConfig)
    return Database.connect(dataSource)
}

private fun mapToHikariConfig(config: DatabaseConfig): HikariConfig =
    HikariConfig().apply {
        jdbcUrl = config.url
        driverClassName = config.driver
        username = config.user
        password = config.password
        config.properties.forEach { addDataSourceProperty(it.key, it.value) }
    }
