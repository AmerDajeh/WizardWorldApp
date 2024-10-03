package com.daajeh.wizardworldapp.domain

import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.coroutines.flow.Flow

interface WizardRepository {

    fun getWizards(): Flow<List<Wizard>>
    fun getWizardById(wizardId: String): Flow<Wizard?>

    suspend fun saveFavouriteWizard(wizardId: String)
    suspend fun removeFavouriteWizard(wizardId: String)

    suspend fun fetchNetworkData(): Result<Unit>
    suspend fun fetchWizardNetworkData(wizardId: String): Result<Unit>
}