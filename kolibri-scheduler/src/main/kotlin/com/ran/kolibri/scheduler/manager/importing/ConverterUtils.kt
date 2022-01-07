package com.ran.kolibri.scheduler.manager.importing

import java.math.BigDecimal

interface ConverterUtils {

    fun contains(string: String, substringsSet: Set<String>): Boolean =
        substringsSet.any { string.lowercase().contains(it) }

    fun bigDecimal(string: String): BigDecimal =
        BigDecimal(string.replace(",", "."))
}
