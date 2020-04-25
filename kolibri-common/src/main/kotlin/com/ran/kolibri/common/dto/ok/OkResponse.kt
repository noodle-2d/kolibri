package com.ran.kolibri.common.dto.ok

data class OkResponse(
    var ok: Boolean? = null
) {
    companion object {
        val VALUE = OkResponse(true)
    }
}
