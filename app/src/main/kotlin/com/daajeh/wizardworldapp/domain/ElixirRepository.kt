package com.daajeh.wizardworldapp.domain

import com.daajeh.wizardworldapp.data.network.dto.LightElixirDto
import com.daajeh.wizardworldapp.domain.entity.LightElixir

interface ElixirRepository {

    suspend fun saveWizardLightElixirs(wizardId: String, elixirs: List<LightElixirDto>)
    suspend fun getWizardLightElixirs(wizardId: String): List<LightElixir>

    suspend fun saveFavouriteElixir(elixirId: String)
    suspend fun removeFavouriteElixir(elixirId: String)
//    suspend fun isFavouriteElixir(elixirId: String): Boolean
}