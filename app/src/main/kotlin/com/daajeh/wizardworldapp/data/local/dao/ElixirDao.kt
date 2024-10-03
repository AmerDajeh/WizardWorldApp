package com.daajeh.wizardworldapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.ElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.wizard.elixir.LightElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.favourite.FavouriteElixirEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ElixirDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(elixir: ElixirEntity)

    @Query("SELECT * FROM elixirs WHERE id=:elixirId")
    fun getElixirById(elixirId: String): Flow<ElixirEntity?>

    // light version
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLight(elixir: LightElixirEntity)

    @Query("SELECT * FROM light_elixirs WHERE wizardId=:wizardId")
    suspend fun getLightElixirsForWizard(wizardId: String): List<LightElixirEntity>

    // favourite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavourite(elixir: FavouriteElixirEntity)

    @Query("DELETE FROM favourite_elixirs WHERE id=:elixirId")
    suspend fun removeFavourite(elixirId: String)

    @Query("SELECT EXISTS(SELECT * FROM favourite_elixirs WHERE id = :elixirId)")
    suspend fun isFavourite(elixirId: String): Boolean
}
