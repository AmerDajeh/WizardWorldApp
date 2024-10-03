package com.daajeh.wizardworldapp.data

import com.daajeh.wizardworldapp.data.local.dao.ElixirDao
import com.daajeh.wizardworldapp.data.local.dao.IngredientDao
import com.daajeh.wizardworldapp.data.local.dao.InventorDao
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ElixirEntity
import com.daajeh.wizardworldapp.data.network.WizardWorldApi
import com.daajeh.wizardworldapp.data.network.dto.LightElixirDto
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ElixirRepositoryImpl(
    private val api: WizardWorldApi,
    private val dao: ElixirDao,
    private val ingredientDao: IngredientDao,
    private val inventorDao: InventorDao
) : ElixirRepository {


    override fun getElixirById(elixirId: String): Flow<Elixir?> =
        dao.getElixirById(elixirId)
            .map { nullableElixir ->
                nullableElixir?.let { elixir ->
                    mapEntityToDomain(elixir)
                } ?: suspendCancellableCoroutine<ElixirEntity> { continuation ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val localLightElixir = dao.getLightElixirById(elixirId)!!
                        val remoteData = api.getElixirDetails(elixirId)
                        val entity = remoteData.toEntity(localLightElixir.wizardId)
                        dao.insert(entity)
                        continuation.resume(entity)
                    }
                }.let { mapEntityToDomain(it) }
            }

    private suspend fun mapEntityToDomain(entity: ElixirEntity): Elixir {
        val ingredients = ingredientDao.getIngredientsForElixir(entity.id)
            .map { it.toDomain() }
        val inventors = inventorDao.getInventorsForElixir(entity.id)
            .map { it.toDomain() }

        return entity.toDomain(
            ingredients = ingredients,
            inventors = inventors,
            isFavourite = dao.isFavourite(entity.id)
        )
    }


    override suspend fun saveWizardLightElixirs(wizardId: String, elixirs: List<LightElixirDto>) {
        elixirs.map { it.toEntity(wizardId) }
            .forEach {
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