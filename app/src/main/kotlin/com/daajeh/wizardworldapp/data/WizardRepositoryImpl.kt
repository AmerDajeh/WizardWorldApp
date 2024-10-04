package com.daajeh.wizardworldapp.data

import com.daajeh.wizardworldapp.data.local.dao.WizardDao
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteWizardEntity
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.WizardRepository
import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WizardRepositoryImpl(
    private val dao: WizardDao,
    private val elixirRepository: ElixirRepository,
) : WizardRepository {

    override fun getWizards(): Flow<List<Wizard>> =
        dao.getWizards()
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
            .getById(wizardId)
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

    override suspend fun toggleFavorite(wizardId: String) =
        dao
            .getById(wizardId)
            .let {
                val isFavorite = dao.isFavourite(wizardId)
                if (isFavorite)
                    dao.removeFavorite(wizardId)
                else {
                    val favourite = FavouriteWizardEntity(wizardId)
                    dao
                        .saveFavourite(favourite)
                }
            }
}