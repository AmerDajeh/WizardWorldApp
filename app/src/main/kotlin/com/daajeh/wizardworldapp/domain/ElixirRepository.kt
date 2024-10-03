package com.daajeh.wizardworldapp.domain

import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.coroutines.flow.Flow

interface ElixirRepository {

    fun getWizards(): Flow<List<Wizard>>
    fun getWizardById(wizardId: String): Flow<Wizard?>

    suspend fun saveFavouriteElixir(elixirId: String)
    suspend fun removeFavouriteElixir(elixirId: String)

    suspend fun saveFavouriteWizard(wizardId: String)
    suspend fun removeFavouriteWizard(wizardId: String)
}