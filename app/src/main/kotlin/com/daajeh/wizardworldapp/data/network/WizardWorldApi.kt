package com.daajeh.wizardworldapp.data.network

import com.daajeh.wizardworldapp.data.network.dto.WizardDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.request
import java.net.URL
import java.util.concurrent.TimeoutException

class WizardWorldApi(
    private val httpClient: HttpClient
) {

    suspend fun getWizards(): Result<List<WizardDto>> =
        try {
            val response =
                httpClient.request(URL("https://wizard-world-api.herokuapp.com/Wizards"))
                    .body<List<WizardDto>>()

            Result.success(response)

        } catch (e: ClientRequestException) {
            Result.failure(e)
        } catch (e: ServerResponseException) {
            Result.failure(e)
        } catch (e: TimeoutException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
}