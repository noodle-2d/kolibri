package com.ran.kolibri.common.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val Any.logger: Logger
    inline get() = LoggerFactory.getLogger(javaClass)

fun Any.logTrace(message: String) {
    val log = logger
    if (log.isTraceEnabled) log.trace(message)
}

fun Any.logTrace(message: String, throwable: Throwable) {
    val log = logger
    if (log.isTraceEnabled) log.trace(message, throwable)
}

fun Any.logDebug(message: String) {
    val log = logger
    if (log.isDebugEnabled) log.debug(message)
}

fun Any.logDebug(message: String, throwable: Throwable) {
    val log = logger
    if (log.isDebugEnabled) log.debug(message, throwable)
}

fun Any.logInfo(message: String) {
    val log = logger
    if (log.isInfoEnabled) log.info(message)
}

fun Any.logInfo(message: String, throwable: Throwable) {
    val log = logger
    if (log.isInfoEnabled) log.info(message, throwable)
}

fun Any.logWarn(message: String) {
    val log = logger
    if (log.isWarnEnabled) log.warn(message)
}

fun Any.logWarn(message: String, throwable: Throwable) {
    val log = logger
    if (log.isWarnEnabled) log.warn(message, throwable)
}

fun Any.logError(message: String) {
    val log = logger
    if (log.isErrorEnabled) log.error(message)
}

fun Any.logError(message: String, throwable: Throwable) {
    val log = logger
    if (log.isErrorEnabled) log.error(message, throwable)
}
