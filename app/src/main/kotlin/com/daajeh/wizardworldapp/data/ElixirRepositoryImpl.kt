package com.daajeh.wizardworldapp.data

import com.daajeh.wizardworldapp.data.local.WizardWorldDatabase
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteWizardEntity
import com.daajeh.wizardworldapp.data.network.WizardWorldApi
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.entity.Wizard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ElixirRepositoryImpl(
    private val api: WizardWorldApi,
    private val database: WizardWorldDatabase
) : ElixirRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            api
                .getWizards()
                .onSuccess {
                    it.forEach { wizard ->
                        database.wizardDao().insert(wizard.toEntity())
                        wizard.elixirs.map { elixir -> elixir.toEntity(wizard.id) }
                            .map { database.elixirDao().insertLight(it) }
                    }
                }
        }
    }

    override fun getWizards(): Flow<List<Wizard>> =
        database
            .wizardDao()
            .getWizards()
            .map { wizardsList ->
                wizardsList.map { wizard ->
                    val isFavourite =  database.wizardDao().isFavourite(wizard.id)
                    val elixirs = database.elixirDao().getLightElixirsForWizard(wizard.id)
                        .map { it.toDomain() }

                    wizard.toDomain(
                        elixirs = elixirs,
                        isFavourite = isFavourite
                    )
                }
            }


    override fun getWizardById(wizardId: String): Flow<Wizard?> =
        database
            .wizardDao()
            .getWizardById(wizardId)
            .map { nullableWizard ->
                nullableWizard?.let { wizard ->
                    val elixirs = database.elixirDao().getLightElixirsForWizard(wizardId)
                        .map { it.toDomain() }
                    val isFavourite = database.elixirDao().isFavourite(wizardId)

                    wizard.toDomain(
                        elixirs = elixirs,
                        isFavourite = isFavourite
                    )
                }
            }


    override suspend fun saveFavouriteElixir(elixirId: String) =
        database
            .elixirDao()
            .getElixirById(elixirId)
            .let {
                val favourite = FavouriteElixirEntity(elixirId)
                database.elixirDao()
                    .saveFavourite(favourite)
            }


    override suspend fun removeFavouriteElixir(elixirId: String) =
        database
            .elixirDao()
            .removeFavourite(elixirId)


    override suspend fun saveFavouriteWizard(wizardId: String) =
        database
            .wizardDao()
            .getWizardById(wizardId)
            .let {
                val favourite = FavouriteWizardEntity(wizardId)
                database.wizardDao()
                    .saveFavourite(favourite)
            }

    override suspend fun removeFavouriteWizard(wizardId: String)  =
        database
            .wizardDao()
            .removeFavourite(wizardId)
}