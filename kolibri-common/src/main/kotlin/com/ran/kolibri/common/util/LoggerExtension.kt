package com.ran.kolibri.common.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val Any.logger: Logger
    inline get() = LoggerFactory.getLogger(javaClass)

inline fun Any.logTrace(message: String) {
    val log = logger
    if (log.isTraceEnabled) log.trace(message)
}

inline fun Any.logTrace(message: String, throwable: Throwable) {
    val log = logger
    if (log.isTraceEnabled) log.trace(message, throwable)
}

inline fun Any.logDebug(message: String) {
    val log = logger
    if (log.isDebugEnabled) log.debug(message)
}

inline fun Any.logDebug(message: String, throwable: Throwable) {
    val log = logger
    if (log.isDebugEnabled) log.debug(message, throwable)
}

inline fun Any.logInfo(message: String) {
    val log = logger
    if (log.isInfoEnabled) log.info(message)
}

inline fun Any.logInfo(message: String, throwable: Throwable) {
    val log = logger
    if (log.isInfoEnabled) log.info(message, throwable)
}

inline fun Any.logWarn(message: String) {
    val log = logger
    if (log.isWarnEnabled) log.warn(message)
}

inline fun Any.logWarn(message: String, throwable: Throwable) {
    val log = logger
    if (log.isWarnEnabled) log.warn(message, throwable)
}

inline fun Any.logError(message: String) {
    val log = logger
    if (log.isErrorEnabled) log.error(message)
}

inline fun Any.logError(message: String, throwable: Throwable) {
    val log = logger
    if (log.isErrorEnabled) log.error(message, throwable)
}
