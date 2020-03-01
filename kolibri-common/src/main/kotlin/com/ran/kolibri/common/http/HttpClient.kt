package com.ran.kolibri.common.http

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class HttpClient {

    private val rest: RestTemplate = RestTemplate()

    fun <RQ, RS> post(url: String, body: RQ, responseClass: Class<RS>): RS {
        val requestEntity = HttpEntity(body)
        val responseEntity = rest.exchange(url, HttpMethod.POST, requestEntity, responseClass)
        return retrieveResponse(responseEntity)
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
