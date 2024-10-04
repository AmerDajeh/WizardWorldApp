package com.daajeh.wizardworldapp.data

import com.daajeh.wizardworldapp.data.local.dao.ElixirDao
import com.daajeh.wizardworldapp.data.local.dao.IngredientDao
import com.daajeh.wizardworldapp.data.local.dao.InventorDao
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteElixirEntity
import com.daajeh.wizardworldapp.data.network.WizardWorldApi
import com.daajeh.wizardworldapp.data.network.dto.LightElixirDto
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class ElixirRepositoryImpl(
    private val api: WizardWorldApi,
    private val dao: ElixirDao,
    private val ingredientDao: IngredientDao,
    private val inventorDao: InventorDao
) : ElixirRepository {

    override fun getElixirById(elixirId: String): Flow<Elixir?> =
        dao.getElixirById(elixirId)
            .combine(ingredientDao.getIngredientsForElixir(elixirId)) { nullableElixir, ingredients ->
                Pair(nullableElixir, ingredients)
            }
            .combine(inventorDao.getInventorsForElixir(elixirId)) { (nullableElixir, ingredients), inventors ->
                nullableElixir?.let { entity ->
                    entity.toDomain(
                        ingredients = ingredients.map { it.toDomain() },
                        inventors = inventors.map { it.toDomain() },
                        isFavourite = dao.isFavourite(entity.id)
                    )
                }
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
        dao.getElixirById(elixirId)
            .let {
                val favourite = FavouriteElixirEntity(elixirId)
                dao
                    .saveFavourite(favourite)
            }

    override suspend fun removeFavouriteElixir(elixirId: String) =
        dao
            .removeFavourite(elixirId)

    override suspend fun fetchElixirNetworkData(elixirId: String): Result<Unit> {
        try {
            dao.getElixirById(elixirId).first()
                ?.let {
                    return Result.success(Unit)
                }

            val localLightElixir = dao.getLightElixirById(elixirId)
                ?: throw RuntimeException("$TAG: can't find light version")

            val remoteData = api.getElixirDetails(elixirId)
            val entity = remoteData.toEntity(localLightElixir.wizardId)
            dao.insert(entity)

            val ingredients = remoteData.ingredients
                .map { it.toEntity(elixirId) }
            val inventors = remoteData.inventors
                .map { it.toEntity(elixirId) }

            ingredients.forEach { ingredientDao.insert(it) }
            inventors.forEach { inventorDao.insert(it) }

            return Result.success(Unit)
        } catch (e: Exception) {
           return Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "ElixirRepository"
    }
}