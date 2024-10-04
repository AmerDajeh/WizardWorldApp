package com.daajeh.wizardworldapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ingredient.IngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingredient: IngredientEntity)

    @Query("SELECT * FROM ingredients WHERE elixirId = :elixirId")
    fun getForElixir(elixirId: String): Flow<List<IngredientEntity>>
}
