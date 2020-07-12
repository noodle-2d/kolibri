package com.ran.kolibri.commandline.utility.service

import java.math.BigDecimal

interface ConverterUtils {

    fun contains(string: String, substringsSet: Set<String>): Boolean =
        substringsSet.any { string.toLowerCase().contains(it) }

    fun bigDecimal(string: String): BigDecimal =
        BigDecimal(string.replace(",", "."))
}
