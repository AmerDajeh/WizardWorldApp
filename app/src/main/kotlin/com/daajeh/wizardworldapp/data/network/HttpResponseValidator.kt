package com.daajeh.wizardworldapp.data.network

import com.daajeh.wizardworldapp.data.network.exceptions.AuthenticationException
import com.daajeh.wizardworldapp.data.network.exceptions.AuthorizationException
import com.daajeh.wizardworldapp.data.network.exceptions.NotFoundException
import com.daajeh.wizardworldapp.data.network.exceptions.ServerException
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.io.IOException

fun HttpClientConfig<*>.httpResponseValidator() {
    install(HttpRequestRetry) {
        maxRetries = 5 // Set the maximum number of retries

        retryIf { _, response ->
            // Retry only for transient server errors (not 5xx) or network issues
            !response.status.isSuccess() && response.status.value !in 500..599
        }

        retryOnExceptionIf { _, cause ->
            cause is IOException || cause is TimeoutCancellationException
        }

        delayMillis { retry ->
            retry * 3000L // Exponential backoff (3s, 6s, 9s, etc.)
        }
    }

    HttpResponseValidator {
        validateResponse { response ->
            // Validate non-2xx responses and handle them appropriately
            when (response.status) {
                HttpStatusCode.OK -> {
                    // No issues, proceed normally
                }

                HttpStatusCode.BadRequest -> {
                    throw IllegalArgumentException("Bad request: ${response.status}")
                }

                HttpStatusCode.Unauthorized -> {
                    throw AuthenticationException("Unauthorized access: ${response.status}")
                }

                HttpStatusCode.Forbidden -> {
                    throw AuthorizationException("Forbidden: ${response.status}")
                }

                HttpStatusCode.NotFound -> {
                    throw NotFoundException("Resource not found: ${response.status}")
                }

                HttpStatusCode.InternalServerError -> {
                    // Server-side issues, won't retry
                    throw ServerException("Internal server error: ${response.status}")
                }

                else -> {
                    throw Exception("Unexpected status code: ${response.status}")
                }
            }
        }
    }
}