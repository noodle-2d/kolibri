package com.ran.kolibri.common.config

import com.ran.kolibri.common.util.tryGetString
import com.typesafe.config.Config
import java.lang.IllegalArgumentException

enum class Environment {
    DEVELOPMENT,
    PRODUCTION;

    companion object {
        fun build(config: Config): Environment =
            when (val env = config.tryGetString("environment")) {
                "development" -> DEVELOPMENT
                "production" -> PRODUCTION
                else -> throw IllegalArgumentException("Unknown environment: $env")
            }
    }
}
