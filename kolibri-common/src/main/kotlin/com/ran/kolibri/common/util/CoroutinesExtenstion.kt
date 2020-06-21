package com.ran.kolibri.common.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> runIO(ioAction: () -> T): T =
    withContext(Dispatchers.IO) { ioAction() }
