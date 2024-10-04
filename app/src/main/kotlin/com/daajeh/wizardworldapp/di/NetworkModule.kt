package com.daajeh.wizardworldapp.di

import android.util.Log
import com.daajeh.wizardworldapp.data.network.WizardWorldApi
import com.daajeh.wizardworldapp.data.network.httpResponseValidator
import com.daajeh.wizardworldapp.presentation.MainActivity
import com.daajeh.wizardworldapp.work.FetchElixirDataWorker
import com.daajeh.wizardworldapp.work.FetchWizardsWorker
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module

private const val TIME_OUT = 6000

val networkModule = module {

    fun createHttpClient(): HttpClient {
        return HttpClient(Android) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                        encodeDefaults = false
                    }
                )
            }

            engine {
                connectTimeout = TIME_OUT
                socketTimeout = TIME_OUT
            }

            // Logging
            install(Logging) {
                level = LogLevel.BODY
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("HttpLogging:", message)
                    }
                }
            }

            // Http Response
            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("HTTP status:", "${response.status.value}")
                }
            }

            // Headers
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            httpResponseValidator()
        }
    }

    scope<MainActivity> {
        scoped {
            createHttpClient()
        }
        scopedOf(::WizardWorldApi)
    }

    scope<FetchWizardsWorker> {
        scoped {
            createHttpClient()
        }
        scopedOf(::WizardWorldApi)
    }

    scope<FetchElixirDataWorker> {
        scoped {
            createHttpClient()
        }
        scopedOf(::WizardWorldApi)
    }
}
