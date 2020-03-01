package com.ran.kolibri.common.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.ran.kolibri.common.util.logError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.io.InputStream
import java.lang.StringBuilder
import java.util.*

@Component
class HttpClient {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val rest: RestTemplate = buildRestTemplate()
    private val headers: HttpHeaders = buildHttpHeaders()

    fun <RQ, RS> post(url: String, body: RQ, responseClass: Class<RS>): RS {
        val requestJson = objectMapper.writeValueAsString(body)
        val requestEntity = HttpEntity(requestJson, headers)
        val responseEntity = rest.exchange(url, HttpMethod.POST, requestEntity, String::class.java)
        val responseJson = responseEntity.body ?: throw HttpClientException("Empty response body")
        return objectMapper.readValue(responseJson, responseClass)
    }

    private fun buildRestTemplate(): RestTemplate {
        val template = RestTemplate()
        template.errorHandler = buildErrorHandler()
        return template
    }

    private fun buildHttpHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON_UTF8
        return headers
    }

    private fun buildErrorHandler(): ResponseErrorHandler =
            object : ResponseErrorHandler {
                override fun hasError(response: ClientHttpResponse): Boolean =
                        !response.statusCode.is2xxSuccessful

                override fun handleError(response: ClientHttpResponse) {
                    logError {
                        "Error while sending request to telegram api. " +
                                "Response status: ${response.rawStatusCode}, ${response.statusCode}. " +
                                "Body: ${readInputStreamAsString(response.body)}"
                    }
                    throw HttpClientException("Error while sending request to telegram api: ${response.statusCode}")
                }
            }

    private fun readInputStreamAsString(inputStream: InputStream): String {
        val scanner = Scanner(inputStream)
        val builder = StringBuilder()
        while (scanner.hasNext()) {
            builder.append(scanner.nextLine())
        }
        return builder.toString()
    }
}

inline fun <RQ, reified RS> HttpClient.post(url: String, body: RQ): RS =
        post(url, body, RS::class.java)
