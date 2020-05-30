package com.ran.kolibri.common.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val Any.log: Logger
    inline get() = LoggerFactory.getLogger(javaClass)
