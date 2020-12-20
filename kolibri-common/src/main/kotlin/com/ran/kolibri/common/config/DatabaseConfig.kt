package com.ran.kolibri.common.config

import com.ran.kolibri.common.util.getStringMap
import com.typesafe.config.Config

data class DatabaseConfig(
    val url: String,
    val driver: String,
    val user: String,
    val password: String,
    val properties: Map<String, String>
) {
    constructor(config: Config) : this(
        config.getString("database.url"),
        config.getString("database.driver"),
        config.getString("database.user"),
        config.getString("database.password"),
        config.getStringMap("database.properties")
    )
}
