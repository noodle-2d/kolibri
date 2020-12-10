package com.ran.kolibri.common.client.sheets.model

data class SheetRange(val rows: List<SheetRow>)

data class SheetRow(val values: List<String>)
