package com.ran.kolibri.common.rest.model

data class OkResponse(
    var ok: Boolean? = null
) {
    companion object {
        val VALUE = OkResponse(true)
    }
}
