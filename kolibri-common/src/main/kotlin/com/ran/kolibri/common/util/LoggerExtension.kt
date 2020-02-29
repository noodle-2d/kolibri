package com.ran.kolibri.common.util

import org.apache.log4j.Level
import org.apache.log4j.Logger

val Any.logger: Logger
    inline get() = Logger.getLogger(javaClass)

inline fun Logger.logInfo(fn: () -> String) {
    if (isInfoEnabled) {
        info(fn.invoke())
    }
}

inline fun Logger.logDebug(fn: () -> String) {
    if (isDebugEnabled) {
        debug(fn.invoke())
    }
}

inline fun Logger.logTrace(fn: () -> String) {
    if (isTraceEnabled) {
        trace(fn.invoke())
    }
}

inline fun Logger.logWarn(fn: () -> String) {
    if (isEnabledFor(Level.WARN)) {
        warn(fn.invoke())
    }
}

inline fun Logger.logWarn(throwable: Throwable, fn: () -> String) {
    if (isEnabledFor(Level.WARN)) {
        warn(fn.invoke(), throwable)
    }
}

inline fun Logger.logError(fn: () -> String) {
    if (isEnabledFor(Level.ERROR)) {
        error(fn.invoke())
    }
}

inline fun Logger.logError(throwable: Throwable, fn: () -> String) {
    if (isEnabledFor(Level.ERROR)) {
        error(fn.invoke(), throwable)
    }
}

inline fun Any.logInfo(fn: () -> String) = logger.logInfo(fn)
inline fun Any.logDebug(fn: () -> String) = logger.logDebug(fn)
inline fun Any.logTrace(fn: () -> String) = logger.logTrace(fn)
inline fun Any.logWarn(fn: () -> String) = logger.logWarn(fn)
inline fun Any.logWarn(throwable: Throwable, fn: () -> String) = logger.logWarn(throwable, fn)
inline fun Any.logError(fn: () -> String) = logger.logError(fn)
inline fun Any.logError(throwable: Throwable, fn: () -> String) = logger.logError(throwable, fn)
