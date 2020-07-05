package com.ran.kolibri.common.dto.config

import com.typesafe.config.Config

data class DatabaseConfig(val url: String, val driver: String, val user: String, val password: String) {
    constructor(config: Config) : this(
        config.getString("database.url"),
        config.getString("database.driver"),
        config.getString("database.user"),
        config.getString("database.password")
    )
}
