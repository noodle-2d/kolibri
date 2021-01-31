package com.ran.kolibri.common.client.sheets

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.ran.kolibri.common.client.sheets.model.GoogleConfig
import java.io.FileInputStream

fun buildSheets(kodein: Kodein): Sheets {
    val googleConfig = kodein.instance<GoogleConfig>()

    val tokensStream = FileInputStream(googleConfig.tokensPath)
    val credential = GoogleCredential
        .fromStream(tokensStream)
        .createScoped(listOf(SheetsScopes.SPREADSHEETS))

    val jsonFactory = JacksonFactory.getDefaultInstance()
    val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

    return Sheets.Builder(httpTransport, jsonFactory, credential)
        .setApplicationName(APPLICATION_NAME)
        .build()
}

private const val APPLICATION_NAME = "kolibri"
