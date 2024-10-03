package com.daajeh.wizardworldapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ingredient.IngredientEntity

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingredient: IngredientEntity)

    @Query("SELECT * FROM ingredients WHERE elixirId = :elixirId")
    suspend fun getIngredientsForElixir(elixirId: String): List<IngredientEntity>
}
