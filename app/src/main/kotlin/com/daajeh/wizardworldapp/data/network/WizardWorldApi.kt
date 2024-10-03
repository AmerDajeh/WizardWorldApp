package com.daajeh.wizardworldapp.data.network

import com.daajeh.wizardworldapp.data.network.dto.ElixirDto
import com.daajeh.wizardworldapp.data.network.dto.WizardDto
import com.daajeh.wizardworldapp.domain.entity.Wizard
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.request
import java.net.URL
import java.util.concurrent.TimeoutException

private const val BASE_URL = "https://wizard-world-api.herokuapp.com"

class WizardWorldApi(
    private val httpClient: HttpClient
) {

    suspend fun getWizards(): Result<List<WizardDto>> =
        try {
            val response =
                httpClient.request(URL("$BASE_URL/Wizards"))
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

    suspend fun getWizardDetails(wizardId: String): Result<WizardDto> {
        val response =
            httpClient.request(URL("/Wizards/$wizardId"))
                .body<WizardDto>()

        return Result.success(response)
    }

    suspend fun getElixirDetails(elixirId: String): ElixirDto{
        val response =
            httpClient.request(URL("$BASE_URL/Elixirs/$elixirId"))
                .body<ElixirDto>()

        return response
    }
}