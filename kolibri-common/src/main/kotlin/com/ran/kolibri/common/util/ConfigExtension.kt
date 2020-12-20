package com.ran.kolibri.common.util

import com.typesafe.config.Config

fun Config.tryGetString(path: String): String? =
    if (hasPath(path)) getString(path) else null

fun Config.getStringMap(path: String): Map<String, String> =
    if (hasPath(path)) getObject(path).mapValues { it.value.render() } else mapOf()
