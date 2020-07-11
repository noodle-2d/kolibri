package com.ran.kolibri.commandline.utility.service

interface ConverterUtils {

    fun contains(string: String, substringsSet: Set<String>): Boolean =
        substringsSet.any { string.toLowerCase().contains(it) }
}
