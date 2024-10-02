package com.daajeh.wizardworldapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.daajeh.wizardworldapp.data.local.dto.IngredientEntity

@Dao
interface IngredientDao {
    @Insert
    suspend fun insert(ingredient: IngredientEntity)

    @Query("SELECT * FROM ingredients WHERE elixirId = :elixirId")
    suspend fun getIngredientsForElixir(elixirId: String): List<IngredientEntity>
}
