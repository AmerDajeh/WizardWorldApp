package com.daajeh.wizardworldapp.domain

import com.daajeh.wizardworldapp.data.network.dto.LightElixirDto
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import kotlinx.coroutines.flow.Flow

interface ElixirRepository {

    fun getElixirById(elixirId: String): Flow<Elixir?>

    suspend fun saveWizardLightElixirs(wizardId: String, elixirs: List<LightElixirDto>)
    suspend fun getWizardLightElixirs(wizardId: String): List<LightElixir>

    suspend fun toggleFavourite(elixirId: String)
    suspend fun removeFavorite(elixirId: String)

    suspend fun fetchElixirNetworkData(elixirId: String): Result<Unit>
}