package com.ran.kolibri.common.http

import com.ran.kolibri.common.util.logDebug
import com.ran.kolibri.common.util.logError
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.lang.RuntimeException

@Component
class HttpClient {

    private val rest: RestTemplate = RestTemplate()

    fun <RQ, RS> post(url: String, body: RQ, responseClass: Class<RS>): RS {
        logDebug { "Send message url: $url" }
        val requestEntity = HttpEntity(body)
        logDebug { "Request entity: $requestEntity" }
        try {
            val responseEntity = rest.exchange(url, HttpMethod.POST, requestEntity, responseClass)
            logDebug { "Response entity: $responseEntity" }
            return retrieveResponse(responseEntity)
        } catch (e: Throwable) {
            logError(e) { "Error sending message" }
            throw RuntimeException("Something went wrong")
        }
    }

    private fun <RS> retrieveResponse(responseEntity: ResponseEntity<RS>): RS =
        responseEntity
                .apply {
                    statusCodeValue.takeIf { it == 200 }
                            ?: throw HttpClientException("Unexpected status code $statusCodeValue")
                }
                .body ?: throw HttpClientException("Empty response body")
}

inline fun <RQ, reified RS> HttpClient.post(url: String, body: RQ): RS =
        post(url, body, RS::class.java)
