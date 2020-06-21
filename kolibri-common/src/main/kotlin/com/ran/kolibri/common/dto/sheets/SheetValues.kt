package com.ran.kolibri.common.dto.sheets

data class SheetRange(val rows: List<SheetRow>)

data class SheetRow(val values: List<String>)
