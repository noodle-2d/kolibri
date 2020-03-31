package com.ran.kolibri.common.dto.config

import com.typesafe.config.Config

data class ServerConfig(val port: Int) {
    constructor(config: Config): this(config.getInt("server.port"))
}
