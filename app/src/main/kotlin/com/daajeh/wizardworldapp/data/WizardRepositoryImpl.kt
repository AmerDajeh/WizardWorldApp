package com.daajeh.wizardworldapp.data

import com.daajeh.wizardworldapp.data.local.dao.WizardDao
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteWizardEntity
import com.daajeh.wizardworldapp.data.network.WizardWorldApi
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class WizardRepositoryImpl(
    private val api: WizardWorldApi,
    private val dao: WizardDao,
    private val elixirRepository: ElixirRepository
) : WizardRepository {

    override fun getWizards(): Flow<List<Wizard>> =
        dao
            .getWizards()
            .map { wizardsList ->
                wizardsList.map { wizard ->
                    val isFavourite = dao.isFavourite(wizard.id)
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

    override suspend fun removeFavouriteWizard(wizardId: String) =
        dao
            .removeFavourite(wizardId)

    override suspend fun fetchNetworkData(): Result<Unit> {
        api
            .getWizards()
            .onSuccess {
                it.forEach { wizard ->
                    dao.insert(wizard.toEntity())
                    elixirRepository.saveWizardLightElixirs(wizard.id, wizard.elixirs)
                }
            }
        return Result.success(Unit)
    }

    override suspend fun fetchWizardNetworkData(wizardId: String): Result<Unit> {
        val wizard = dao.getWizardById(wizardId).first()
            ?: return Result.failure(RuntimeException("can't find wizard in cache"))

        wizard
            .toDomain(
                elixirs = elixirRepository.getWizardLightElixirs(wizardId),
                isFavourite = dao.isFavourite(wizardId)
            )

        return Result.success(Unit)
    }
}