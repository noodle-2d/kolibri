package com.ran.kolibri.common.client.sheets.model

import com.typesafe.config.Config

data class GoogleConfig(
    val credentials: GoogleCredentials,
    val accountsSpreadsheetId: String
) {
    constructor(config: Config) : this(
        config
            .getConfig("google.credentials")
            .let { GoogleCredentials(it) },
        config.getString("google.accounts-spreadsheet-id")
    )
}

data class GoogleCredentials(
    val type: String,
    val projectId: String,
    val privateKeyId: String,
    val privateKey: String,
    val clientEmail: String,
    val clientId: String,
    val authUri: String,
    val tokenUri: String,
    val authProviderX509CertUrl: String,
    val clientX509CertUrl: String
) {
    constructor(config: Config) : this(
        config.getString("type"),
        config.getString("project-id"),
        config.getString("private-key-id"),
        config.getString("private-key")
            .replace("\\n", "\n"), // To resolve problems with new lines in env variables
        config.getString("client-email"),
        config.getString("client-id"),
        config.getString("auth-uri"),
        config.getString("token-uri"),
        config.getString("auth-provider-x509-cert-url"),
        config.getString("client-x509-cert-url")
    )
}
