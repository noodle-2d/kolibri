package com.ran.kolibri.telegram.bot.dto.ok

data class OkResponse(
        var ok: Boolean? = null
) {
    companion object {
        val VALUE = OkResponse(true)
    }
}
