package com.daajeh.wizardworldapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.daajeh.wizardworldapp.data.local.dto.ElixirEntity

@Dao
interface ElixirDao {
    @Insert
    suspend fun insert(elixir: ElixirEntity)

    @Query("SELECT * FROM elixirs WHERE id=:elixirId")
    suspend fun getElixirById(elixirId: String): ElixirEntity

    @Query("SELECT * FROM elixirs")
    suspend fun getAllElixirs(): List<ElixirEntity>
}
