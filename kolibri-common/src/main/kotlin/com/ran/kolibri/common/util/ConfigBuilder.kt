package com.ran.kolibri.common.util

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValue
import com.typesafe.config.ConfigValueFactory

fun buildConfig(args: Array<String>): Config {
    val argsConfig = buildArgsConfig(args)
    val fileConfig = ConfigFactory.load()
    return argsConfig.withFallback(fileConfig)
}

private fun buildArgsConfig(args: Array<String>): Config =
    args.fold(ConfigFactory.empty()) { config, arg ->
        val (key, value) = parseArg(arg)
        config.withValue(key, value)
    }

private fun parseArg(arg: String): Pair<String, ConfigValue> =
    when (val index = arg.indexOf("=")) {
        -1 -> {
            val value = ConfigValueFactory.fromAnyRef(true)
            Pair(arg, value)
        }
        else -> {
            val value = ConfigValueFactory.fromAnyRef(arg.drop(index + 1))
            Pair(arg.take(index), value)
        }
    }
