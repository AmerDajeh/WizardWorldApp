package com.daajeh.wizardworldapp.data

import com.daajeh.wizardworldapp.data.local.dao.ElixirDao
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteElixirEntity
import com.daajeh.wizardworldapp.data.network.WizardWorldApi
import com.daajeh.wizardworldapp.data.network.dto.LightElixirDto
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.entity.LightElixir

class ElixirRepositoryImpl(
    private val api: WizardWorldApi,
    private val dao: ElixirDao
) : ElixirRepository {

    override suspend fun saveWizardLightElixirs(wizardId: String, elixirs: List<LightElixirDto>) {
        elixirs.map { it.toEntity(wizardId) }
            .forEach{
                dao.insertLight(it)
            }
    }

    override suspend fun getWizardLightElixirs(wizardId: String): List<LightElixir> =
        dao.getLightElixirsForWizard(wizardId)
            .map { it.toDomain() }

    override suspend fun saveFavouriteElixir(elixirId: String) =
        dao
            .getElixirById(elixirId)
            .let {
                val favourite = FavouriteElixirEntity(elixirId)
                dao
                    .saveFavourite(favourite)
            }


    override suspend fun removeFavouriteElixir(elixirId: String) =
        dao
            .removeFavourite(elixirId)

}