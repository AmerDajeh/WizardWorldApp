package com.daajeh.wizardworldapp.data

import com.daajeh.wizardworldapp.data.local.dao.WizardDao
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteWizardEntity
import com.daajeh.wizardworldapp.data.network.WizardWorldApi
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WizardRepositoryImpl(
    private val api: WizardWorldApi,
    private val dao: WizardDao,
    private val elixirRepository: ElixirRepository
) : WizardRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            api
                .getWizards()
                .onSuccess {
                    it.forEach { wizard ->
                        dao.insert(wizard.toEntity())
                        elixirRepository.saveWizardLightElixirs(wizard.id, wizard.elixirs)
                    }
                }
        }
    }

    override fun getWizards(): Flow<List<Wizard>> =
        dao
            .getWizards()
            .map { wizardsList ->
                wizardsList.map { wizard ->
                    val isFavourite =  dao.isFavourite(wizard.id)
                    val elixirs = elixirRepository.getWizardLightElixirs(wizard.id)

                    wizard.toDomain(
                        elixirs = elixirs,
                        isFavourite = isFavourite
                    )
                }
            }


    override fun getWizardById(wizardId: String): Flow<Wizard?> =
        dao
            .getWizardById(wizardId)
            .map { nullableWizard ->
                nullableWizard?.let { wizard ->
                    val elixirs = elixirRepository.getWizardLightElixirs(wizard.id)

                    val isFavourite = dao.isFavourite(wizardId)

                    wizard.toDomain(
                        elixirs = elixirs,
                        isFavourite = isFavourite
                    )
                }
            }

    override suspend fun saveFavouriteWizard(wizardId: String) =
        dao
            .getWizardById(wizardId)
            .let {
                val favourite = FavouriteWizardEntity(wizardId)
                dao
                    .saveFavourite(favourite)
            }

    override suspend fun removeFavouriteWizard(wizardId: String)  =
        dao
            .removeFavourite(wizardId)

    override suspend fun fetchNetworkData(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchWizardNetworkData(wizardId: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}