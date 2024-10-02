package com.daajeh.wizardworldapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.daajeh.wizardworldapp.data.local.dto.ElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.FavouriteElixirEntity
import com.daajeh.wizardworldapp.data.local.dto.LightElixirEntity

@Dao
interface FavouriteElixirDao {

    @Insert
    suspend fun save(elixir: FavouriteElixirEntity)

    @Query("DELETE FROM favourite_elixirs WHERE id=:elixirId")
    suspend fun remove(elixirId: String)

    @Query("SELECT EXISTS(SELECT * FROM favourite_elixirs WHERE id = :elixirId)")
    suspend fun isFavourite(elixirId: String): Boolean
}
