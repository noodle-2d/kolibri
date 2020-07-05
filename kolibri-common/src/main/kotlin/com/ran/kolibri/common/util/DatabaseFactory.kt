package com.ran.kolibri.common.util

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.ran.kolibri.common.dto.config.DatabaseConfig
import org.jetbrains.exposed.sql.Database

fun buildDatabase(kodein: Kodein): Database {
    val config = kodein.instance<DatabaseConfig>()
    return Database.connect(
        config.url,
        config.driver,
        config.user,
        config.password
    )
}
