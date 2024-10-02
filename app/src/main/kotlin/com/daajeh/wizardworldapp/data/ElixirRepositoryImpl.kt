package com.daajeh.wizardworldapp.data

import com.daajeh.wizardworldapp.data.local.WizardWorldDatabase
import com.daajeh.wizardworldapp.data.local.dto.FavouriteElixirEntity
import com.daajeh.wizardworldapp.domain.ElixirRepository
import com.daajeh.wizardworldapp.domain.entity.Elixir
import com.daajeh.wizardworldapp.domain.entity.LightElixir
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ElixirRepositoryImpl(
    private val database: WizardWorldDatabase
) : ElixirRepository {

    override fun getElixirs(): Flow<List<LightElixir>> =
        database
            .lightElixirDao()
            .getByDifficulty("")
            .map { elixirsList ->
                elixirsList.map { elixir ->
                    elixir.toDomain(
                        database.favouriteElixirDao().isFavourite(elixir.id)
                    )
                }
            }


    override fun getElixirById(elixirId: String): Flow<Elixir?> =
        database
            .elixirDao()
            .getElixirById(elixirId)
            .map { elixir ->
                val ingredients = database.ingredientDao().getIngredientsForElixir(elixirId)
                    .map { it.toDomain() }
                val inventors = database.inventorDao().getInventorsForElixir(elixirId)
                    .map { it.toDomain() }
                val isFavourite = database.favouriteElixirDao().isFavourite(elixirId)

                elixir.toDomain(
                    ingredients = ingredients,
                    inventors = inventors,
                    isFavourite = isFavourite
                )
            }


    override suspend fun saveFavourite(elixirId: String) =
        database
            .elixirDao()
            .getElixirById(elixirId)
            .let {
                val favourite = FavouriteElixirEntity(elixirId)
                database.favouriteElixirDao()
                    .save(favourite)
            }


    override suspend fun removeFavourite(elixirId: String) =
        database
            .favouriteElixirDao()
            .remove(elixirId)
}