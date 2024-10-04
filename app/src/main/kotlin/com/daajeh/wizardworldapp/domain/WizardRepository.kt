package com.daajeh.wizardworldapp.domain

import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.coroutines.flow.Flow

interface WizardRepository {

    fun getWizards(): Flow<List<Wizard>>
    fun getWizardById(wizardId: String): Flow<Wizard?>

    suspend fun saveFavorite(wizardId: String)
    suspend fun removeFavorite(wizardId: String)

    suspend fun fetchNetworkData(): Result<Unit>
    suspend fun fetchWizardNetworkData(wizardId: String): Result<Unit>
}